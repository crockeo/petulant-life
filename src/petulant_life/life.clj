(ns petulant-life.life
  (:require [clojure.pprint :refer [pprint]]))

;; This code was taken from Clojure Programming by Chas Emerick, Brian
;; Carper, and Christophe Grand (O’Reilly).

;; Copyright 2012 Chas Emerick, Brian Carper, and Christophe Grand,
;; 978-1-449-39470-7.

(defn empty-board
  "Creates a rectangular empty board of the specified width
   and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(defn populate
  "Turns :on each of the cells specified as [y, x] coordinates."
  [board living-cells]
  (reduce (fn [board coordinates]
            (assoc-in board coordinates :on))
          board
          living-cells))

(defn hex-neighbours
  [[x y]]
  (for [dx [-1 0 1] dy (if (zero? dx) [-2 2] [-1 1])]
    [(+ dx x) (+ dy y)]))

(defn rect-neighbours
  [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1] :when (not= 0 dx dy)]
    [(+ dx x) (+ dy y)]))

(defn stepper
  "Returns a step function for Life-like cell automata.
  neighbours takes a location and return a sequential collection
  of locations. survive? and birth? are predicates on the number
  of living neighbours."
  [neighbours birth? survive?]
  (fn [cells]
    (set (for [[loc n] (frequencies (mapcat neighbours cells))
               :when (if (cells loc) (survive? n) (birth? n))]
           loc))))

(def rect-step (stepper rect-neighbours  #{3} #{2 3}))

(def hex-step  (stepper hex-neighbours   #{2} #{3 4}))

(defn run [step-n]
  (->> (iterate rect-step #{[2 0] [2 1] [2 2] [1 2] [0 1]})
       (drop (dec step-n))
       first
       (populate (empty-board 6 6))
       pprint))
