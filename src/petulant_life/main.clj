(ns petulant-life.main
  (:import (org.lwjgl.opengl DisplayMode
                             Display
                             GL11))

  (:require [petulant-life.rendering :as r]))

; Defining some width and height constraints to the program.
(def width 640)
(def height 480)

; Initializing the OpenGL window.
(defn create []
  (Display/setDisplayMode (DisplayMode. width height))
  (Display/create)

  (GL11/glMatrixMode GL11/GL_PROJECTION)
  (GL11/glLoadIdentity)
  (GL11/glOrtho 0 width height 0 -1 1)
  (GL11/glMatrixMode GL11/GL_MODELVIEW))

; Cleaning up the LWJGL context.
(defn destroy []
  (Display/destroy))

; Rendering a square.
(defn renderSquare [x y w h]
  (GL11/glColor3f 1 1 1)
  (GL11/glBegin GL11/GL_QUADS)
    (GL11/glVertex2f x y)
    (GL11/glVertex2f (+ x w) y)
    (GL11/glVertex2f (+ x w) (+ y h))
    (GL11/glVertex2f x (+ y h))
  (GL11/glEnd))

; Running the simulation / graphics.
(defn run []
  (loop [running true]
    (if (and running (not (Display/isCloseRequested)))
      (do (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
          (renderSquare 10 10 50 50)
          (Display/update)
          (recur true))
      nil)))

; Entry point.
(defn -main [& args]
  (create)
  (run)
  (destroy))
