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
;; note that we would also know the number of crates from the length of string
;; and we can use (range) instead of doing the whole crate-indices thing.
;; reading the input saves me from potential typos though like 0-based indexing
;; while the crates are using 1-based...

;; can we zipmap an assoc with some sort of comp seq/range magic?
(first crates)
;; ok first we need to extract the characters from between the brackets
(map last (partition 2 (first crates)))
;; roughly double what we want because we dont filter out the \space delimiters
;; between crates..
(->> crates
     first
     (partition 4)
     (map second))
;; partitining by 4 we can just consume the spaces. The second character of every 4
;; characters would be the crate contents.
;; [ a ] \space [ b ] \space [ \space  ]. empty crates replace brackets with spaces
;; so
;; [ a ] \space [ b ] \space \space \space \space
;; stride by 4
;; [ a ] \space
;; [ b ] \space
;; [ \space ]
;; every second: a b \space

(re-seq #"\w+" (apply str (last buckets)))
;; and we can now clean this up by creating a map of sequence and its index
(zipmap (map second buckets)
        (->> buckets
             count
             range))
;; => (0 1 2 3 4 5 6 7 8)

(concat [\Z] [\G] [\V] [\V] [\Q] [\M] [\L] [\N] [\R])
