(ns petulant-life.main
  (:import (org.lwjgl.opengl DisplayMode
                             Display)))

(def width 640)
(def height 480)

; Initializing the OpenGL window.
(defn create []
  (Display/setDisplayMode (DisplayMode. width height))
  (Display/create))

; Cleaning up the LWJGL context.
(defn destroy []
  (Display/destroy))

; Entry point.
(defn -main [& args]
  (create)
  (destroy))
