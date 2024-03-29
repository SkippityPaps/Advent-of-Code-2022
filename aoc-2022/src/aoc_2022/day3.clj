(ns aoc-2022.day3
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.java.io :as io]))

(def rucksacks (->> "day3"
                    io/resource
                    io/input-stream
                    slurp
                    str/split-lines))

(defn partition-bag-into-compartments
  "Return a pair of compartments containing the first and second halves of the
  input bag."
  [bag]
  (partition (/ (count bag) 2) bag))

(defn get-duplicate-item-in-bag
  "Takes both compartments of a bag and intersects them to find any duplicates
  within the bag. "
  [bag]
  (->> bag
       (map set)
       (apply set/intersection)))

;; create range of numbers from ascii value of a to value of z
;; map back to char to get a range of characters a-z
(defn char-range [a z]
  (->>
   (range (int a) (inc (int z)))
   (map char)))

(defn char-to-key [c]
  (->> c
       str
       keyword))

;; priorities:
;; range 1-26 = a-z
;; range 27-52 = A-Z
;; this is pretty gnarly looking I'm sure there's a better way
(def priorities
  "Letters a to Z mapped to values 1 to 52."
  (let [alphabet (concat (char-range \a \z) (char-range \A \Z))
        alphalength (count alphabet)]
    (zipmap
     (map char-to-key alphabet)
     (range 1 (inc alphalength)))))

;; PART 1 - get sum of priorities of unique items in all rucksacks
(->> rucksacks
     (map partition-bag-into-compartments)
     (mapcat get-duplicate-item-in-bag)
     (map (comp priorities char-to-key))
     (reduce +))

(comment
  (->> rucksacks
       (mapcat (comp get-duplicate-item-in-bag
                     partition-bag-into-compartments))
       (map (comp priorities
                  char-to-key))
       (reduce +)))

;; PART 2
(def elf-groups
  (partition 3 rucksacks))

(defn find-badge-value [elf-group]
  (->> elf-group
       (map set)
       (apply set/intersection)))

(def group-badges (mapcat find-badge-value elf-groups))
(def group-priorities (map (comp priorities char-to-key) group-badges))
(reduce + group-priorities);; => 2552
