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
     (map second)) ;; failing to capture last crate input for some reason.
;; oh because im not using partition-all and there's only 3 characters in the
;; last group. replacing (partition 4) with (partition-all 4) should fix it.
(->> crates
     first
     (partition-all 4)
     (map second)) ;; yep. fixed.
;; i think i prefer partition n step col form of this though
(->> crates
     first
     (partition 2 4)
     (map last))

(defn parse-crate-line [line]
  "Parameter 'line' represents a row in the stack of crates."
  (some->> line
           (partition 2 4)
           (map second)))

(->> crates
     first
     parse-crate-line)

(map parse-crate-line crates) ;; it really is that easy

;; partitining by 4 we can just consume the spaces. The second character of every 4
;; characters would be the crate contents.
;; \space [ a ] \space [ b ] \space [ \space  ]. empty crates replace brackets with spaces
;; so
;; \space [ a ] \space [ b ] \space \space \space
;; stride by 4
;; \space [ a ]
;; \space [ b ]
;; \space [ \space ]
;; every second: a b \space

;; the above ramblings about partition strides and crate contents aren't very 
;; great comments... I kind of get what I was trying to convey.
(map second (partition 4 (first crates)))
(map last (partition 2 4 (first crates)))
(concat [\space] [] [\V] [\V] [\Q] [\M] [\L] [\N] [\R])

;; so where are we right now? 
;; we need to take the list of moves and parse them into something meaningful.
;; right now we're just reading crates? We're trying to build up a data struct
;; to apply the 'moves' to. 

(reverse crates)
;; walk in reverse so we can build up our crate stacks easily. 
;; assoc-in crate column/stack index per newline and we will have
;; our stacks ready to be moved around. 

crate-indices

(rest
 (map parse-crate-line (reverse crates)))

(def crate-stacks
  (zipmap crate-indices (repeat (count crate-indices) [])))

;;(apply merge-with conj [crate-stacks test-map test-map2])

;; all of this repeated use of '(zipmap crate-indices ...) has me thinking
;; we can create a helper partial such that: 
(def to-crate-map (partial zipmap crate-indices))

(apply merge-with conj crate-stacks
       (->> crates
            reverse
            rest
            (map parse-crate-line)
            (map #(zipmap crate-indices %))))
 ;; clean the above up to make it a little more readable, even if more verbose
(defn create-crate-stacks [stacks]
  (let [reverse-order-crates (rest (reverse crates))
        crate-contents (map parse-crate-line reverse-order-crates)
        crates-as-maps (map to-crate-map crate-contents)]))



(map to-crate-map (map parse-crate-line (rest (reverse crates))))