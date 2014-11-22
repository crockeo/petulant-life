(ns petulant-life.main
  (:import (org.lwjgl.opengl DisplayMode
                             Display
                             GL11
                             ContextAttribs
                             PixelFormat))

  (:require [clojure.set :refer [union]]
            [petulant-life.rendering :as r]
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

;; Returns true if the random chance succeeded. False otherwise.
(defn chancey [chance]
  (let [rand (rand-int 100)]
    (if (< rand chance)
      true
      false)))

;; Generating a random board.
(defn generate-board [chance minx maxx miny maxy]
  (reduce union #{} (flatten (map (fn [x]
                                    (map (fn [y]
                                           (if (chancey chance)
                                             #{[x y]}
                                             #{}))
                                         (range miny maxy)))
                                  (range minx maxx)))))

;; Running the simulation / graphics.
(defn run [shader stepper]
  (let [colors [[1 0 0 0.5]
                [0 1 0 0.5]
                [0 0 1 0.5]]]
    (loop [boards [(generate-board 30 0 50 0 50)
                   (generate-board 30 0 50 0 50)
                   (generate-board 30 0 50 0 50)]]
      (when-not (Display/isCloseRequested)
        (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
        (dorun
         (map (fn [b c]
                (r/draw-rectangles (life->screen 5 2 b)
                                   shader
                                   c))
              boards colors))

        (Display/update)
        (Thread/sleep 100)
        (recur (map stepper boards))))))

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
