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
  (let [shader (load-shader-raw path type)]
    shader)) ;; TODO: Check for compilation.

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
    {vert :vert
     frag :frag
     geom :geom}))

;; Attaching a bunch of shaders to a shader program if they exist.
(defn attach-if-exists [shaders program]
  (if (get shaders :vert)
    (GL20/glAttachShader program (get shaders :vert))
    nil)

  (if (get shaders :frag)
    (GL20/glAttachShader program (get shaders :frag))
    nil)

  (if (get shaders :geom)
    (GL20/glAttachShader program (get shaders :geom))
    nil))

;; Loading a whole shader program.
(defn load-shader-program-raw [src-path]
  (let []
    nil)) ;; TODO: Load the program.

;; Loading a whole shader program - and then checking if it linked.
(defn load-shader-program [src-path]
  (let [shader-program (load-shader-program-raw src-path)]
    shader-program)) ;; TODO: Check for linking.
