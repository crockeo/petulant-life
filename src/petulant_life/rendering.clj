(ns petulant-life.rendering
  (:import (org.lwjgl.opengl GL30
                             GL20
                             GL15
                             GL11)

           (org.lwjgl BufferUtils)))

; Creating a VertexArrayObject out of a set of vertices.
(defn withVAO [vs callback]
  (let [fvs (flatten vs)
        vct (/ (count fvs) 3)
        bvs (BufferUtils/createFloatBuffer (count fvs))
        vbo (GL15/glGenBuffers)
        vao (GL30/glGenVertexArrays)]
    (do ; Setting up the buffered vertices.
        (.put bvs fvs)
        (.flip bvs)

        ; Binding the VAO.
        (GL30/glBindVertexArray vao)

        ; Binding and setting the VBO.
        (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
        (GL15/glBufferData GL15/GL_ARRAY_BUFFER bvs GL15/GL_STATIC_DRAW)

        ; Loading the VBO into the VAO.
        (GL20/glVertexAttribPointer 0 3 GL11/GL_FLOAT false 0 0)

        ; Performing the callback with the VAO.
        (callback vao)

        ; Unbinding and cleaning up the VBO and VAO for now.
        (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)
        (GL15/glDeleteBuffers vbo)

        (GL30/glBindVertexArray 0)
        (GL30/glDeleteVertexArrays(vao)))))

; Generating the vertices for a rectangle. Despite it being a rectangle, it
; requires 6 points to be rendered using using a VAO.
(defn generateVerts [x y w h]
  [[x y 0]
   [(+ x w) y 0]
   [x (+ y h) 0]
   [(+ x w) y 0]
   [x (+ y h) 0]
   [(+ x w) (+ y ) 0]])

; Loading a shader. It loads all possible extensions for the shader. If there is
; a .vert, it'll load it. If there's a .frag, it'll load it, etc.
(defn loadShaderProgram [path]
  nil)

; Drawing based on the information contained in a VAO with a given shader.
(defn drawVAO [shader vao]
  nil)

; Drawing a single rectangle using a shader.
(defn drawRectangle [x y w h shader]
  (withVAO (generateVerts x y w h) (partial drawVAO shader)))
