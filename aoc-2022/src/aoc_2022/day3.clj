(ns aoc-2022.day3
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.java.io :as io]))

;;; i need to split *each rucksack* into groups of 2 compartments
(defn into-halves [collection]
  (->>
   (partition (/ (count collection) 2) collection)
   (map #(apply str %))))

(def test-bags '("vJrwpWtwJgWrhcsFMMfFFhFp"
                 "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
                 "PmmdzqPrVvPwwTWBwg"
                 "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
                 "ttgJtRGJQctTZtZT"
                 "CrZsJsPPZsGzwwsLwLmpwMDw"))

(def rucksacks (->> "day3"
                    io/resource
                    io/input-stream
                    slurp
                    str/split-lines))

;; from here, we need to take each string ("bag/rucksack") and split it into 2
;; equal halves. Our goal is to find the common character in the two
;; half-strings.

(def temp-bag (first rucksacks)) ;; => "jNNBMTNzvTqhQLhQLMQL"
(-> temp-bag
    count
    (/ 2)
    (split-at temp-bag))
;; => [(\j \N \N \B \M \T \N \z \v \T) (\q \h \Q \L \h \Q \L \M \Q \L)]
;; above should be the same as partition?

(def a-bag (-> temp-bag
               count
               (/ 2)
               (partition temp-bag)))
;; => ((\j \N \N \B \M \T \N \z \v \T) (\q \h \Q \L \h \Q \L \M \Q \L))

(apply set/intersection (map set a-bag))

(map into-halves test-rucks)
(def compartments (map into-halves test-rucks))
(def compartments (map #(map set %) compartments))
(clojure.set/intersection (first (first (map #(map set %) compartments)))
                          (second (first (map #(map set %) compartments))))

(first compartments)
(reduce clojure.set/intersection (first compartments))

(map #(reduce intersection %) compartments)

(def compartments (map into-halves rucksacks))

;;; create range of numbers from ascii value of a to value of z
;;; map back to char to get a range of characters a-z
;;; we need Aa-Zz so our range shoud be A-z
(defn char-range [a z]
  (->>
   (range (int a) (inc (int z)))
   (map char)))

(char-range \a \z)
;; => (\a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z)
(char-range \A \Z)
;; => (\A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q \R \S \T \U \V \W \X \Y \Z)

;;; priorities
; range 1-26 = a-z
; range 27-52 = A-Z
(concat (char-range \a \z) (char-range \A \Z))
(def priorities-a (zipmap (map (comp keyword str) (char-range \a \z)) (range 1 27)))
(def priorities-b (zipmap (map (comp keyword str) (char-range \A \Z)) (range 27 53)))
priorities-a
priorities-b
(def priorities (merge priorities-a priorities-b)) ;; this is fine since we're using unique keys

;;; two different versions of char-to-key. I find the first more readable.
(defn char-to-key [c]
  (->> c
       str
       keyword))

(defn char-to-key* [c]
  ((comp keyword str) c))

(char-to-key 'a)
(char-to-key* 'a)

(map char-to-key (char-range \a \z))

(def common-items
  (->>
   (map #(map set %) compartments)
   (map #(reduce set/intersection %))
   (map #(apply str %))
   (map #(keyword %))
   (map priorities)))

(reduce + common-items)

;;;; lets try to clean up/simplify the code
(defn get-intersection-as-key [bag]
  (->> bag
       (map set)
       (reduce set/intersection)
       (apply str)
       keyword))

(defn intersection-priority [k]
  (priorities k))
(map get-intersection-as-key compartments)

(->> compartments
     (map #(map set %)))

(set/intersection (set (first (first compartments))) (set (second (first compartments))))

(defn bag-intersection [bag]
  (let [compartment-a (set (first bag))
        compartment-b (set (second bag))]
    ((apply (comp keyword str)
            (set/intersection compartment-a compartment-b))
     priorities)))

(map bag-intersection compartments)

(comment
  (defn string-intersection [a b]
    (set/intersection (set a) (set b)))
  (string-intersection "jNNBMTNzvT" "qhQLhQLMQL")
  (map (fn [x y] (string-intersection x y)) compartments)

  (map (fn [a b] (string-intersection a b)) (first compartments))

  (first compartments)
;; => ("jNNBMTNzvT" "qhQLhQLMQL")

  (->> intersection-bag
       (apply str)
       keyword)
  (map print (map str (partition 2 '(1 2 3 4))))
  (map (fn [a b] (str a " and " b)) (partition 2 1 '(1 2 3 4)))

  (def simple-items
    (->> bag
         (map (comp str/join set))))

  (map #(apply str %) (partition-all 2 '(1 2 3 4)))

  (->> bag
       first
       set
       str/join)

  (map (fn [k] (map #(% priorities) k))))
