(ns petulant-life.shader
  (:require [clojure.java.io :refer [as-file]])
  (:import (org.lwjgl.opengl GL32
                             GL20)))

;; Loading a single shader.
(defn load-shader-raw [path type]
  (try
    (let [content (StringBuffer. (slurp path))
          sid (GL20/glCreateShader type)]
      (do (GL20/glShaderSource sid content)
          (GL20/glCompileShader sid)
          sid))
    (catch Exception e (.exit System 1))))

;; Loading a shader and then checking for if it compiled correctly.
(defn load-shader [path type]
  (let [shader (load-shader-raw path type)
        status (GL20/glGetShaderi shader GL20/GL_COMPILE_STATUS)]
    (if (= status 1)
      shader
      (print (str "SHADER COMPILER ERROR: " (GL20/glGetShaderInfoLog shader 2048))))))

;; Loading a shader if it exists.
(defn load-if-exists [path type]
  (if (.exists (as-file path))
    (load-shader path type)
    nil))

;; Loading a bunch of shaders and returning them in one map.
(defn load-shaders [src-path]
  (let [vert (load-if-exists (str src-path ".vert") GL20/GL_VERTEX_SHADER)
        frag (load-if-exists (str src-path ".frag") GL20/GL_FRAGMENT_SHADER)
        geom (load-if-exists (str src-path ".geom") GL32/GL_GEOMETRY_SHADER)]
    {:vert vert
     :frag frag
     :geom geom}))

;; Attaching a bunch of shaders to a shader program if they exist.
(defn attach-if-exists [shader-program shaders]
  (dorun
   (map (fn [[key shader]]
          (when shader
            (GL20/glAttachShader shader-program shader)))
        shaders)))

;; Loading a whole shader program.
(defn load-shader-program-raw [src-path]
  (let [shader-program (GL20/glCreateProgram)]
    (attach-if-exists shader-program (load-shaders src-path))
    (GL20/glLinkProgram shader-program)
    shader-program))

;; Loading a whole shader program - and then checking if it linked.
(defn load-shader-program [src-path]
  (let [shader-program (load-shader-program-raw src-path)
        status (GL20/glGetProgrami shader-program GL20/GL_LINK_STATUS)]
    (if (= status 1)
      shader-program
      (print (str "SHADER LINKER ERROR: " (GL20/glGetProgramInfoLog shader-program 2048))))))
