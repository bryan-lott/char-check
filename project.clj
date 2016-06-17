(defproject char-check "0.1.0-SNAPSHOT"
  :description "Command line until to check that a given file has a given set of characters."
  :url "https://github.com/bryan-lott/char-check"
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.taoensso/timbre "4.0.1"]  ;; logging
                 [org.clojure/tools.cli "0.3.5"]]  ;; command line args
  :target-path "target/%s"
  :main ^:skip-aot char-check.core
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Xms4g" "-Xmx4g" "-server"]}
             :dev {:plugins [[com.jakemccrary/lein-test-refresh "0.12.0"]
                             [lein-kibit "0.1.2"]]}})
