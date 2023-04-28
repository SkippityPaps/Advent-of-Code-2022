(ns aoc-2022.day2
  (:require [clojure.string :as str]))

(def guide (slurp "data/day2"))
(def parsed-guide (map (fn [pair] (str/split pair #" ")) (str/split-lines guide)))
(def parsed-guide (map (fn [round] (map keyword round)) parsed-guide))

; total score = sum of score for all rounds
; round score = shape + outcome (loss = 0, draw = 3, win = 6)

(comment (def relationships
           {:A {:X 3 :Y 6 :Z 0} ;rock/rock draw. rock/paper lose. rock/scissors win.
            :B {:X 0 :Y 3 :Z 6}
            :C {:X 6 :Y 0 :Z 3}}) 
         
         (def shape-value
           {:X 1 :Y 2 :Z 3})
         )
; hold on. 
; A, B, C is the opponent
; X, Y, Z is my play.
; so if oppponent plays scissors, C, and I play rock X I should win. 
(defn play-round [round]
  (get-in relationships round))
(defn calc-shape-value-for-round [round]
  ((second round) shape-value))
(defn calc-final-player-score [round]
  (+ (play-round round)
     (calc-shape-value-for-round round)))

(reduce + (map calc-final-player-score parsed-guide))

;;; WE CAN SIMPLIFY THIS GREATLY BY EMBEDDING SHAPE VALUE INTO FINAL SCORE 
;;; +1 for x, +2 for y, +3 for z
(def relationships
  {:A {:X 4 :Y 8 :Z 3} 
   :B {:X 1 :Y 5 :Z 9} 
   :C {:X 7 :Y 2 :Z 6}}) 
  
; now it just turns into adding it all up. simple.
(reduce + (map play-round parsed-guide))