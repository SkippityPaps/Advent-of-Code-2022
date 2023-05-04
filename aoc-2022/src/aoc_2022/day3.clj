(ns aoc-2022.day3
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.java.io :as io]))


(def rucksacks-resource (io/resource "day3"))
(def rucksacks-input-stream (io/input-stream rucksacks-resource))

(def rucksacks
  (let [rucksack-resource (io/resource "day3")
        rucksack-input (io/input-stream rucksack-resource)
        rucks (slurp rucksack-input)
        rucks (str/split-lines rucks)]
    rucks))

;;; i need to split *each rucksack* into groups of 2 compartments
(defn into-halves [collection]
  (->>
   (partition (/ (count collection) 2) collection)
   (map #(apply str %) )))

(def test-rucks '("vJrwpWtwJgWrhcsFMMfFFhFp"
"jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
"PmmdzqPrVvPwwTWBwg"
"wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
"ttgJtRGJQctTZtZT"
"CrZsJsPPZsGzwwsLwLmpwMDw"))


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
(defn char-range [a z] (->>
(range (int a) (inc (int z)))
(map char)))

(char-range \a \z)
(char-range \A \Z)

;;; priorities
; range 1-26 = a-z
; range 27-52 = A-Z
(def priorities-a (zipmap (map (comp keyword str) (char-range \a \z)) (range 1 27)))
(def priorities-b (zipmap (map (comp keyword str) (char-range \A \Z)) (range 27 53)))
priorities-a
priorities-b
(def priorities (merge priorities-a priorities-b)) ;; this is fine since we're using unique keys

(def common-items
  (->>
    (map #(map set %) compartments)
    (map #(reduce set/intersection %))
    (map #(apply str %))
    (map #(keyword %))
    (map priorities)))

(reduce + common-items)





(def bag (first compartments))
bag
(def simple-items
  (->> bag
       (map (comp str/join set))))

(->> bag
     first
     set
     str/join)

    (map (fn [k] (map #(% priorities) k)) ))


