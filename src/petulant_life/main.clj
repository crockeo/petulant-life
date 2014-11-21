(ns petulant-life.main
  (:import (org.lwjgl.opengl DisplayMode
                             Display
                             GL11
                             ContextAttribs
                             PixelFormat))

  (:require [petulant-life.rendering :as r]
            [petulant-life.shader :as s]))

;; Defining some width and height constraints to the program.
(def window-width 640)
(def window-height 480)

;; Initializing the OpenGL window.
(defn create []
  (let [bgi 0.9]
    (Display/setDisplayMode (DisplayMode. window-width window-height))
    (Display/create (PixelFormat.)
                    (-> (ContextAttribs. 3 2)
                        (.withForwardCompatible    true)
                        (.withProfileCore true)))
    (Display/setTitle "Petulant Life")
    (Display/setResizable true)
    (GL11/glClearColor bgi bgi bgi 1)))


;; Cleaning up the LWJGL context.
(defn destroy []
  (Display/destroy))

;; Running the simulation / graphics.
(defn run [shader]
  (while (not (Display/isCloseRequested))
    (do (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
        (r/draw-rectangles [[0 0 1 1]      ;; For testing no shader-based scaling.
                            [10 10 50 50]] ;; For testing    shader-based scaling.
                           shader)
        (Display/update))))

(defmacro with-cleanup [close-fn & body]
  `(try
     (do ~@body)
     (finally
       (~close-fn))))

;; Entry point.
(defn -main [& args]
  (with-cleanup destroy
    (create)
    (run (s/load-shader-program "resources/gol"))))
