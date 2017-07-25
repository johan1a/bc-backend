(ns bc-backend.data-util
  (:gen-class)
  (:require [clojure.string :as str]))

(defn get-lines
  [file-name]
  (let [file (slurp file-name)
        raw-lines (str/split file #"\n")
        lines (map #(str/split % #",") raw-lines)]
    (drop 1 lines)))


; The value that represents missing data
(def max-val 1.7e+308) 

(defn fix-missing-data
  " Replaces missing rows with the previous row 
    Assumes the first row is OK. "
  ([lines] (fix-missing-data lines 1))
  ([lines i]
   (if (= (count lines) i)
    lines
   (let [prev (nth lines (- i 1))
         row (nth lines i)
         largest (last (sort row))]
     (if (>= largest max-val)
        (recur (assoc lines i prev) (inc i))
        (recur lines (inc i)))))))

(defn parse-closes
  [raw-lines]
  (let [lines      (fix-missing-data raw-lines)
        data       (map (fn [line]  [(nth line 0) (nth line 4)]) lines)]
    data))

(defn parse-data
  [raw-lines]
  (let [lines      (fix-missing-data raw-lines)
        dates           ( map #(nth % 0) lines)
        opens           ( map #(nth % 1) lines)
        high            ( map #(nth % 2) lines)
        low             ( map #(nth % 3) lines)
        closes          ( map #(nth % 4) lines)
        volume-btc      ( map #(nth % 5) lines)
        volume-curr     ( map #(nth % 6) lines)
        weighted-price  ( map #(nth % 7) lines)]
    {
     :dates dates
     :opens opens
     :closes closes
     :volume-btc volume-btc
     :volume-curr volume-curr
     :weighted-price weighted-price
     }))

(defn load-data
  []
  (parse-data (get-lines "bitstamp-usd-btc.csv")))
