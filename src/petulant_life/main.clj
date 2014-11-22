(ns petulant-life.main
  (:import (org.lwjgl.opengl DisplayMode
                             Display
                             GL11
                             ContextAttribs
                             PixelFormat))

  (:require [petulant-life.rendering :as r]
            [petulant-life.config :as c]
            [petulant-life.shader :as s]
            [petulant-life.life   :as l]))

;; Initializing the OpenGL window.
(defn create []
  (let [bgi 0.9]
    (Display/setDisplayMode (DisplayMode. c/window-width c/window-height))
    (Display/create (PixelFormat.)
                    (-> (ContextAttribs. 3 2)
                        (.withForwardCompatible    true)
                        (.withProfileCore true)))
    (Display/setTitle "Petulant Life")
    (Display/setResizable true)

    (GL11/glEnable GL11/GL_BLEND)
    (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)
    (GL11/glClearColor bgi bgi bgi 1)))


;; Cleaning up the LWJGL context.
(defn destroy []
  (Display/destroy))

(defn life->screen [size padding life-board]
  (let [coord (fn [x] (+ (* padding
                            (inc x))
                         (* size x)))]
    (map (fn [[x y]]
           [(coord x) (coord y)
            size size])
         life-board)))

;; Running the simulation / graphics.
(defn run [shader stepper]
  (let [colors [[1 0 0 1]
                [0 1 0 1]
                [0 0 1 1]]]
   (loop [board [#{[2 0] [2 1] [2 2] [1 2] [0 1]}
                 #{[4 12] [3 12] [4 10] [4 11] [2 11]}
                 #{[9 25] [9 23] [9 24] [7 24] [8 25]}]]
     (when-not (Display/isCloseRequested)
       (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
       (dorun
        (map (fn [b c]
               (r/draw-rectangles (life->screen 5 2 b)
                                  shader
                                  c))
             board colors))

       (Display/update)
       (Thread/sleep 100)
       (recur (map stepper board))))))

(defmacro with-cleanup [close-fn & body]
  `(try
     (do ~@body)
     (finally
       (~close-fn))))

(def torus-step (l/stepper (l/mk-torus-neighbours [91 68]) #{3} #{2 3}))

;; Entry point.
(defn -main [& args]
  (with-cleanup destroy
    (create)
    (run (s/load-shader-program "resources/gol")
      torus-step)))
