(defproject petulant-life "0.1.0-SNAPSHOT"
  :description "Music-reactive game of life."
  :url "http://github.com/crockeo/petulant-life"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :main petulant-life.main
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [overtone "0.9.1"]
                 [org.lwjgl.lwjgl/lwjgl "2.9.1"]
                 [org.lwjgl.lwjgl/lwjgl-platform "2.9.1"
                  :classifier ~(str "natives-"
                                    (let [os-name (System/getProperty "os.name")]
                                     (cond
                                      (= "Mac OS X"
                                         os-name) "osx"
                                      (re-find #"^Windows" os-name) "windows"
                                      :else "linux")))

                  ;; LWJGL stores natives in the root of the jar; this
                  ;; :native-prefix will extract them.
                  :native-prefix ""]]
  :native-path "native")
