(ns char-check.core-test
  (:require [clojure.test :refer :all]
            [char-check.core :refer :all]))

(deftest test-str->test-map
  (testing "Easy case."
    (is (str->test-map "ABCDE")
        {"A" false
         "B" false
         "C" false
         "D" false
         "E" false})))
