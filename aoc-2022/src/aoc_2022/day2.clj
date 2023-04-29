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
(count relationships)
(take 2 relationships)
  
; now it just turns into adding it all up. simple.
(reduce + (map play-round parsed-guide))

;;; PART 2
(def round-outcome
  "Sets of conditions to lose, tie, and win a round given an opponent's played
   shape (A, B, or C).
   If A is played, to lose you should play C, to tie play A, and to win play B.
   If B is played, to lose you should play A, to tie play B, and to win play C.
   If C is played, to lose you should play B, to tie play C, and to win play A."
  {:A {:X :C :Y :A :Z :B}
   :B {:X :A :Y :B :Z :C}
   :C {:X :B :Y :C :Z :A}})
;;; something we know is that if we play B to win against A we have win 
;;; condition (6) + paper played to win = 6+2 = total score would be 8
;;; we can simplify that map above to the final outcomes
(def final-round-outcome
  {:A {:X 3 :Y 4 :Z 8}
   :B {:X 1 :Y 5 :Z 9}
   :C {:X 2 :Y 6 :Z 7}})
;;; There some sort of pattern here but it's not entirely apparent ^^^

; :A :X -> rock is played. I need to lose. Play scissors (:C)
; :A :Y -> rock is played. I need to draw. Play rock (:A)
; :A :Z -> rock is played. I need to win. Play paper (:B)
(def shape-values (zipmap (keys relationships) (range 1 (inc (count relationships)))))
;(get-in round-outcome (first parsed-guide))
(->> parsed-guide 
     (map #(get-in round-outcome %))
     (map #(get shape-values %)))

(->> parsed-guide
     (map #(get-in final-round-outcome %))
     (reduce +))

