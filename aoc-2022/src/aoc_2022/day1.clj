(ns aoc-2022.day1
  (:require [clojure.string :as str]))

(def calories (str/split-lines (slurp "data/day1")))
(def grouped-calories (partition-by str/blank? calories))
(def grouped-calories (remove #(every? str/blank? %) grouped-calories))


(defn accumulate-calories [calories] 
  (->> (map #(Integer/parseInt %) calories)
       (reduce +)))

;;; PART 1
(->> (map accumulate-calories grouped-calories)
     sort
     last)

;;; PART 2
(->> (map accumulate-calories grouped-calories)
     sort
     reverse
     (take 3)
     (reduce +))
