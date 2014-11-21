(ns petulant-life.rendering
  (:require [clojure.java.io :refer [as-file]]
            [petulant-life.main :refer [window-width window-height]])

  (:import (org.lwjgl.opengl ARBSeparateShaderObjects
                             GL41
                             GL32
                             GL30
                             GL20
                             GL15
                             GL11)

           (org.lwjgl BufferUtils)))

;; Creating a VertexArrayObject out of a set of vertices.
(defn with-vao [vs callback]
  (let [fvs (flatten vs)
        vct (/ (count fvs) 3)
        bvs (BufferUtils/createFloatBuffer (count fvs))
        vbo (GL15/glGenBuffers)
        vao (GL30/glGenVertexArrays)]
    (do ;; Setting up the buffered vertices.
        (.put bvs (into-array Float/TYPE fvs))
        (.flip bvs)

        ;; Binding the VAO.
        (GL30/glBindVertexArray vao)

        ;; Binding and setting the VBO.
        (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
        (GL15/glBufferData GL15/GL_ARRAY_BUFFER bvs GL15/GL_STATIC_DRAW)

        ;; Loading the VBO into the VAO.
        (GL20/glVertexAttribPointer 0 2 GL11/GL_FLOAT false 0 0)

        ;; Performing the callback with the VAO.
        (callback vao vct)

        ;; Unbinding and cleaning up the VBO and VAO for now.
        (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)
        (GL15/glDeleteBuffers vbo)

        (GL30/glBindVertexArray 0)
        (GL30/glDeleteVertexArrays vao))))

;; Generating the vertices for a rectangle. Despite it being a rectangle, it
;; requires 6 points to be rendered using using a VAO.
(defn generate-rectangle [[x y w h]]
  [[x y]
   [x (+ y h)]
   [(+ x w) (+ y h)]
   [x y]
   [(+ x w) (+ y h)]
   [(+ x w) y]])

;; Generating a number of rectangles.
(defn generate-rectangles [rects]
  (flatten (map generate-rectangle rects)))

;; Drawing based on the information contained in a VAO with a given shader.
(defn draw-vao [shader vao vct]
  ;; Setting up the VAO.
  (GL30/glBindVertexArray vao)
  (GL20/glEnableVertexAttribArray 0)

  ;; Using a shader.
  (GL20/glUseProgram shader)

  ;; Setting the uniform for the window size.
  (GL20/glUniform2f 0 window-width window-height)

  ;; Actually drawing the square.
  (GL11/glDrawArrays GL11/GL_TRIANGLES 0 (* 2 vct))

  ;; Disabling the shader.
  (GL20/glUseProgram 0)

  ;; Cleaning up the VAO.
  (GL20/glDisableVertexAttribArray 0)
  (GL30/glBindVertexArray 0))

;; Drawing a single rectangle using a shader.
(defn draw-rectangle [x y w h shader]
  (with-vao (generate-rectangle x y w h) (partial draw-vao shader)))

;; Drawing a number of rectangles using a shader.
(defn draw-rectangles [rects shader]
  (with-vao (generate-rectangles rects) (partial draw-vao shader)))
