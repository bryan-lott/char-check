(ns char-check.core
  (:require
   [taoensso.timbre :as log]
   [taoensso.timbre.appenders.core :as appenders]
   [clojure.tools.cli :refer [parse-opts]]
   [clojure.java.io :refer [as-file]]
   [clojure.string :refer [split join]])
  (:gen-class))

(defn exit
  "Exits the program with a status code and message."
  [status msg]
  (println msg)
  (System/exit status))

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
  "Given a map of characters and a lazy sequence of lines, remove chars in the lines
  from the map of characters, ending if either characters is empty or we're out of lines."
  [characters lines]
  (loop [characters characters
         lines lines]
    (if (or (empty? characters)
            (empty? lines))
      characters
      (recur (dissoc-from-test characters (process-line (first lines))) (rest lines)))))

(defn main
  "Main function.  Calling without a file object results in reading from STDIN (*in*).
  Otherwise, opens the in-file as a stream.
  Processes the stream until all characters are found.
  Exits with number of characters not found."
  ([characters]
   (main characters (java.io.BufferedReader. *in*)))
  ([characters in-file]
   (with-open [r in-file]
     (let [chars-not-found (keys (run-file (str->test-map characters) (line-seq r)))]
       (when (seq chars-not-found)
         (exit (count chars-not-found) (str "Characters not found in file:\n" (join chars-not-found))))
       (println "All characters (" characters ") found")))))




(def cli-options
  [["-u" "--upper" "Check for uppercase letters [A-Z]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "ABCDEFGHIJKLMNOPQRSTUVWXYZ"}))]
   ["-l" "--lower" "Check for lowercase letters [a-z]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdefghijklmnopqrstuvwxyz"}))]
   ["-n" "--number" "Check for numbers [0-9]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "0123456789"}))]
   ["-6" "--hex" "Check for hexidecimal numbers [0-9a-f]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "abcdef0123456789"}))]
   ["-p" "--punctuation" "Check for common punctuation [.,?!&-'\";:]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters ".,?!&-'\";:"}))]
   ["-s" "--symbol" "Check for symbols [`~!@#$%^&_-+*/=(){}[]|\\:;\"'<,>.?}]"
    :assoc-fn (fn [m k _] (merge-with str m {:characters "`~!@#$%^&_-+*/=(){}[]|\\:;\"'<,>.?}"}))]
   ["-w" "--whitespace" "Check for whitespace, tabs, spaces, newlines"
    :assoc-fn (fn [m k _] (merge-with str m {:characters (str \tab \newline \space \formfeed \return)}))]
   ["-h" "--help"]])

(defn -main
  "Entrypoint, parses arguments, exits with any errors, provides args to main."
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        summary (str "Usage: java -jar char-check.jar [OPTION]... [FILE]\n"
                     "Check for the existence of character classes in FILE or standard input.\n\n"
                     summary
                     "\nWith no FILE, read standard input.\n\n"
                     "Examples:\n"
                     "    java -jar char-check.jar -l test_file.txt\n"
                     "    echo \"abcdefghijklmnopqrstuvwxy\" | java -jar char-check.jar -l")]
    (cond
      (:help options) (exit 0 summary)
      errors (exit 1 (join "\n" errors))
      (empty? arguments) (main (:characters options))
      :else (main (:characters options) (clojure.java.io/reader (first arguments))))))


