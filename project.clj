(defproject petulant-life "0.1.0-SNAPSHOT"
  :description "Music-reactive game of life."
  :url "http://github.com/crockeo/petulant-life"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :main petulant-life.main
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.lwjgl.lwjgl/lwjgl "2.9.1"]]
  :jvm-opts [~(str "-Djava.library.path=native/:" (System/getProperty "java.library.path"))])
