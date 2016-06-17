(ns char-check.core-test
  (:require [clojure.test :refer :all]
            [char-check.core :refer :all]))

(deftest test-str->test-map
  (testing "Easy case."
    (is (= (str->test-map "ABCDE")
           {"A" nil
            "B" nil
            "C" nil
            "D" nil
            "E" nil}))))

(deftest test-process-line
  (testing "No repeating characters."
    (is (= (process-line "abcdefABCDEF")
           #{"a" "b" "c" "d" "e" "f" "A" "B" "C" "D" "E" "F"})))
  (testing "Empty string"
    (is (= (process-line "")
           #{})))
  (testing "Repeating characters."
    (is (= (process-line "aaaaabcde")
           #{"a" "b" "c" "d" "e"}))))

(deftest test-dissoc-from-test
  (testing "Empty empty"
    (is (= (dissoc-from-test {} #{})
           {})))
  (testing "Map empty set"
    (is (= (dissoc-from-test {"a" nil} #{})
           {"a" nil})))
  (testing "map set no remove"
    (is (= (dissoc-from-test {"a" nil} #{"b"})
           {"a" nil})))
  (testing "map set remove one"
    (is (= (dissoc-from-test {"a" nil "b" nil} #{"b"})
           {"a" nil})))
  (testing "map set remove all"
    (is (= (dissoc-from-test {"a" nil "b" nil} #{"a" "b"})
           {}))))

(deftest test-run-file
  (let [lines ["abcde" "ABCDE" "01234"]]
   (testing "Normal run"
     (is (= (run-file (str->test-map "abcdeABCDE01234") lines)
            {})))
   (testing "Run without finding everything"
     (is (= (run-file (str->test-map "abcdefABCDEF012345") lines)
            {"f" nil "F" nil "5" nil})))))
