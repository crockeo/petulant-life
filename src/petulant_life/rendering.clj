(ns petulant-life.rendering
  (:require [clojure.java.io :refer [as-file]])

  (:import (org.lwjgl.opengl ARBSeparateShaderObjects
                             GL41
                             GL32
                             GL30
                             GL20
                             GL15
                             GL11)

           (org.lwjgl BufferUtils)))

;; Creating a VertexBufferObject out of a set of vertices.
(defn make-vbo [vs]
  (let [fvs (flatten vs)
        bvs (BufferUtils/createFloatBuffer (count fvs))
        vbo (GL15/glGenBuffers)]
    (.put bvs (into-array Float/TYPE fvs))
    (.flip bvs)

    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER bvs GL15/GL_STATIC_DRAW)

    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)

    vbo))

;; Deleting a VertexBufferObject.
(defn delete-vbo [vbo]
  (GL15/glDeleteBuffers vbo))

;; Creating a VertexArrayObject from a VertexBufferObject.
(defn make-vao [vbo]
  (let [vao (GL30/glGenVertexArrays)]
    (do
      (GL30/glBindVertexArray vao)
      (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
      (GL20/glVertexAttribPointer 0 2 GL11/GL_FLOAT false 0 0)
      (GL30/glBindVertexArray 0)
      vao)))

;; Deleting a VertexArrayObject.
(defn delete-vao [vao]
  (GL30/glBindVertexArray 0)
  (GL30/glDeleteVertexArrays vao))

;; Performing some action in the context of having access to a
;; VertexBufferObject, a VertexArrayObject, and a count of the vertices.
(defmacro with-vao [vs & body]
  (let [vbo (make-vbo vs)
        vao (make-vao vbo)
        vct (count vs)]
    (GL30/glBindVertexArray vao)
    (GL20/glEnableVertexAttribArray 0)

    ~body

    (delete-vao vao)
    (delete-vbo vbo)))

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

;; Drawing a rectangle.
(defn draw-rectangle [spec]
  (with-vao (generate-rectangle spec) ;; Throws up an error during compile (but
                                      ;; it's a RuntimeExcepion?))
    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 (* 2 vct))))
