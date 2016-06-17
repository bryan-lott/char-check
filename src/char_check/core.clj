(ns char-check.core
  (:require
    [taoensso.timbre :as log]
    [taoensso.timbre.appenders.core :as appenders]
    [clojure.tools.cli :refer [parse-opts]]
    [clojure.java.io :refer [as-file]]
    [clojure.string :refer [split join]])
  (:gen-class))


(defn str->test-map
  "Converts a string of characters to a unique map with keys of each letter.
  The values determine whether that particular character has been found."
  [s]
  (zipmap (map str s) (repeat nil)))

(defn process-line
  "Given a string, produce a set of strings."
  [s]
  (set (map str s)))

(defn dissoc-from-test
  "Given a map and a set, dissoc from the map all keys in the set."
  [m s]
  (apply dissoc m s))

(defn run-file
  "Given a map of characters and a lazy sequence of lines, remove chars in the file
  from the map of characters, ending if either characters is empty or we're out of lines."
  [characters lines]
  (loop [characters characters
         lines lines]
    (if (or (empty? characters)
            (empty? lines))
      characters
      (recur (dissoc-from-test characters (process-line (first lines))) (rest lines)))))

(defn main [characters in-file]
  "Main function.  Opens the in-file and processes the file until all characters are found.
  Exits with number of characters not found."
  (with-open [r (clojure.java.io/reader in-file)]
    (let [chars-remaining (run-file (str->test-map characters) (line-seq r))]
      (when (seq chars-remaining)
        (println "Characters not found in file:\n" (join (keys chars-remaining)))
        (System/exit (count (keys chars-remaining))))
      (println "All characters (" characters ") found"))))



(def cli-options
  [["-u" "--upper" "Check for uppercase letters [A-Z]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "ABCDEFGHIJKLMNOPQRSTUVWXYZ"}))]
   ["-l" "--lower" "Check for lowercase letters [a-z]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdefghijklmnopqrstuvwxyz"}))]
   ["-n" "--number" "Check for numbers [0-9]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "0123456789"}))]
   ["-h" "--hex" "Check for hexidecimal numbers [0-9a-f]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdef0123456789"}))]
   ["-p" "--punctuation" "Check for common punctuation [.,?!&-'\";:]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdef0123456789"}))]
   ["-s" "--symbol" "Check for symbols [`~!@#$%^&_-+*/=(){}[]|\\:;\"'<,>.?}]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdef0123456789"}))]
   ["-f" "--file FILEPATH" "Location of the file under test."
    :default nil
    :parse-fn str
    :validate [#(.exists (as-file %)) "File does not exist!"]]
   ["-h" "--help"]])

(defn -main
  "Entrypoint, parses arguments, exits with any errors, provides args to main."
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (when errors
      (dorun (map println errors))
      (System/exit 1))
    (main (:characters options) (:file options))))
