(ns aoc-2022.day5
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(some->> (io/resource "day5")
         io/input-stream
         slurp
         read-string)

(some->> (io/resource "day5")
         io/reader
         line-seq
         (take 5))

;; our crate stack visualization is separated by the move list by one blank
;; string.
;; we can turn the file into a line-seq and partition-by that one blank string
;; to turn our input into "crates" and "moves" for easier handling
(def crates-and-moves (some->> (io/resource "day5")
                               io/reader
                               line-seq
                               (partition-by str/blank?)
                               ((juxt first last))))
(def crates (first crates-and-moves))
(def moves (last crates-and-moves))

(def crate-indices
  "Reads last string of 'crates' \" 0 1 2 ...\" as a sequence of integers."
  (->>
   (re-seq #"\d+" (last crates))
   (map read-string)))

(filter (comp not str/blank?)
        (map str
             (map last (partition 2 "[Z] [G] [V] [V] [Q] [M] [L] [N] [R]"))))
;; we could always strip away the brackets if we just reseq for chars...
(re-seq #"\w+" "[Z] [G] [V] [V] [Q] [M] [L] [N] [R]")
;; => ("Z" "G" "V" "V" "Q" "M" "L" "N" "R")
;; this doesnt work for whitespace though
;; "                    [L]     [H] [W]"
(re-seq #"\w+" "                    [L]     [H] [W]")
;; we dont know the index of any of the three buckets by doing this.
;; we know that a bucket in string is 3 characters large so we could
;; also try partitioning by 3 and skipping 1 (whitespace)
(def buckets (partition-all 4 "                    [L]     [H] [W]"))
;; => ((\space \space \space \space) (\space \space \space \space) (\space \space \space \space) (\space \space \space \space) (\space \space \space \space) (\[ \L \] \space) (\space \space \space \space) (\[ \H \] \space) (\[ \W \]))
;; lets clean this up by

;; we could also just take every other character from the character sequence (string) which would give us letter, \space, letter, \space, etc...
(reduce (fn [_ x] x) "                    [L]     [H] [W]")

(re-seq #"\w+" (apply str (last buckets)))
;; and we can now clean this up by creating a map of sequence and its index
(zipmap (map second buckets)
        (->> buckets
             count
             range))
;; => (0 1 2 3 4 5 6 7 8)

(concat [\Z] [\G] [\V] [\V] [\Q] [\M] [\L] [\N] [\R])
