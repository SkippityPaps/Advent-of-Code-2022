(ns aoc-2022.day4
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def raw-elf-pairs (some->> (io/resource "day4")
                            io/input-stream
                            slurp
                            str/split-lines))

(defn parse-raw-elf-pair [elf-pair]
  (->> elf-pair
       (re-seq #"\d+")
       (map read-string)))

(def raw-elf-ranges (map parse-raw-elf-pair raw-elf-pairs))

(defn irange [start end]
  (range start (inc end)))

(defn elf-ranges-as-sets [both-ranges]
  (->> both-ranges
       (partition 2)
       (map (comp set (partial apply irange)))))

(def parsed-elf-ranges (map elf-ranges-as-sets raw-elf-ranges))

(defn range-in-range? [both-ranges]
  (->> both-ranges
       (apply
        (juxt set/superset? set/subset?))
       (not-every? false?)))

;; part 1
;; is one range fully contained in another given a pair of ranges?
(->> parsed-elf-ranges
     (map range-in-range?)
     (filter true?)
     count)

;; part 2
;; are there any elements of one range in another?
(->> parsed-elf-ranges
     (map (partial apply set/intersection))
     (filter seq)
     count) ;; => 849
