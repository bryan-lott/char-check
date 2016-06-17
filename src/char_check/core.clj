(ns char-check.core
  (:require
    [taoensso.timbre :as log]
    [taoensso.timbre.appenders.core :as appenders]
    [clojure.tools.cli :refer [parse-opts]]
    [clojure.java.io :refer [as-file]]))


(defn str->test-map
  "Converts a string of characters to a unique map with keys of each letter.
  The values determine whether that particular character has been found."
  [s]
  (zipmap (map str s) (repeat false)))

(def cli-options
  [["-u" "--upper" "Check for uppercase letters [A-Z]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "ABCDEFGHIJKLMNOPQRSTUVWXYZ"}))]
   ["-l" "--lower" "Check for lowercase letters [a-z]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdefghijklmnopqrstuvwxyz"}))]
   ["-n" "--number" "Check for numbers [0-9]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "0123456789"}))]
   ["-f" "--file FILEPATH" "Location of the file under test."
    :default nil
    :parse-fn str
    :validate [#(.exists (as-file %)) "File does not exist!"]]])

(defn main [characters infile]
  (with-open [r (clojure.java.io/reader in-file)]))



(apply dissoc {"a" true "b" true "c" true} (map str "ab"))
(map str"ab")
(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (when errors
      (dorun (map println errors))
      (System/exit 1))
    (main (:characters options) (:file options))))



(-main "-uln" "-f" "dev-resources/test-file.txt")
