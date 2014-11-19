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
        (.put bvs (into-array Float/TYPE fvs))
        (.flip bvs)

        ; Binding the VAO.
        (GL30/glBindVertexArray vao)

        ; Binding and setting the VBO.
        (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo)
        (GL15/glBufferData GL15/GL_ARRAY_BUFFER bvs GL15/GL_STATIC_DRAW)

        ; Loading the VBO into the VAO.
        (GL20/glVertexAttribPointer 0 3 GL11/GL_FLOAT false 0 0)

        ; Performing the callback with the VAO.
        (callback vao vct)

        ; Unbinding and cleaning up the VBO and VAO for now.
        (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)
        (GL15/glDeleteBuffers vbo)

        (GL30/glBindVertexArray 0)
        (GL30/glDeleteVertexArrays vao))))

; Generating the vertices for a rectangle. Despite it being a rectangle, it
; requires 6 points to be rendered using using a VAO.
(defn generateRectangle [[x y w h]]
  [[x y 0]
   [x (+ y h) 0]
   [(+ x w) (+ y h) 0]
   [x y 0]
   [(+ x w) (+ y h) 0]
   [(+ x w) y 0]])

; Generating a number of rectangles.
(defn generateRectangles [rects]
  (flatten (map generateRectangle rects)))

; Loading a specific type of shader at a given path.
(defn loadShader [path type]
  (try
    (let [content (StringBuffer. (slurp path))
          sid     (GL20/glCreateShader type)]
      (do (GL20/glShaderSource sid content)
          (GL20/glCompileShader sid)
          sid))
    (catch Exception e (.exit System 1)))) ; So very unsafe.

; Loading a shader. It loads all possible extensions for the shader. If there is
; a .vert, it'll load it. If there's a .frag, it'll load it, etc.
(defn loadShaderProgram [path]
  (use 'clojure.java.io)
  nil)

; LOAD A SHADER :)

; Drawing based on the information contained in a VAO with a given shader.
(defn drawVAO [shader vao vct]
  (GL30/glBindVertexArray vao)
  (GL20/glEnableVertexAttribArray 0)
  (GL11/glDrawArrays GL11/GL_TRIANGLES 0 (* 2 vct))
  (GL20/glDisableVertexAttribArray 0)
  (GL30/glBindVertexArray 0))

; Drawing a single rectangle using a shader.
(defn drawRectangle [x y w h shader]
  (withVAO (generateRectangle x y w h) (partial drawVAO shader)))

; Drawing a number of rectangles using a shader.
(defn drawRectangles [rects shader]
  (withVAO (generateRectangles rects) (partial drawVAO shader)))
