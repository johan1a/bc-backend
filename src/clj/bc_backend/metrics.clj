(ns bc-backend.metrics
  (:require [clojure.string :as str]
            [bc-backend.bitcoincharts-api :as bcc-api]))

(defn positive?
  [x]
  (>= x 0))

(defn gain-from-diff
  [diff]
  (if (> diff 0) diff 0))

(defn loss-from-diff
  [diff]
  (if (<= diff 0) (- diff) 0))

(defn weighted-avg
  [prev curr n]
  (/ (+ (* prev (- n 1)) curr) n))

(defn initial-avgs
  [all-diffs n i]
  (let [diffs (take n (drop (- i n) all-diffs))
        gains (map gain-from-diff diffs)
        losses (map loss-from-diff diffs)
        avg-gain (/ (reduce + gains) n)
        avg-loss (/ (reduce + losses) n)
        ]
    [avg-gain avg-loss]))

(defn calc-avgs
  ([diffs n avg-gains avg-losses] (calc-avgs diffs n avg-gains avg-losses n))
  ([diffs n avg-gains avg-losses i]
   (if (= i (- (count diffs) 0))
     {:avg-gains avg-gains :avg-losses avg-losses}
     (let [prev-avg-gain (last avg-gains)
           prev-avg-loss (last avg-losses)
           diff (nth diffs i)
           gain (gain-from-diff diff)
           loss (loss-from-diff diff)
           avg-gain (weighted-avg prev-avg-gain gain n)
           avg-loss (weighted-avg prev-avg-loss loss n) 
           ]
       (recur diffs n (conj avg-gains avg-gain) (conj avg-losses avg-loss) (inc i))))))

(defn get-avgs
  ([diffs n] (get-avgs diffs n (- n 1)))
  ([diffs n i]
   (let [initials (initial-avgs diffs n i)
         avg-gains [(first initials)]
         avg-losses [(second initials)]
         avgs (calc-avgs diffs n avg-gains avg-losses (inc i))]
     avgs)))

(defn rsi-from-avgs 
  [avg-gain avg-loss]
  (if (zero? avg-loss)
    100
    (let [relative-strength (/ avg-gain avg-loss)]
      (- 100 (/ 100 (+ 1 relative-strength))))))

(defn get-diffs
  [values]
  (let [first-days (take (- (count values) 1) values)
        second-days (drop 1 values)]
    (map - second-days first-days)))

(defn get-rsis
  [data-points n]
  (let [diffs (get-diffs data-points)
        avgs (get-avgs diffs n)
        avg-gains (:avg-gains avgs)
        avg-losses (:avg-losses avgs)
        rsis (map rsi-from-avgs avg-gains avg-losses)]
    (concat (repeat n nil) rsis)))

(defn rsi
  [periods]
  (let [data (:closes (bcc-api/get-all-data))]
    (get-rsis data periods)))

(defn average
  "Average of the n data points starting at index i"
  [all-data-points n i]
  (let [data-points (take n (drop i all-data-points))
        sum- (reduce + data-points)]
   (/ sum- n)))

(defn calc-sma
  [data-points n]
  (let [range- (range 0 (- (count data-points) n))
        smas (map #(average data-points n %) range-)
        nils (vec (repeat (- n 1) nil))]
    (concat nils smas)))

(defn sma
  [periods]
  (let [data (:closes (bcc-api/get-all-data)) ]
        (calc-sma data periods)))

(defn calc-ema
  ([data-points n]
  (let [initial-avg (average data-points n 0)
        multiplier (float (/ 2 (+ n 1)))
        initial-emas (conj (vec (repeat (- n 1) nil)) initial-avg)]
    (calc-ema (drop n data-points) n multiplier initial-emas)))
  ([data-points n multiplier prev-emas]
  (if (empty? data-points) prev-emas
    (let [close (first data-points)
          prev-ema (last prev-emas)
          ema (+ (* (- close prev-ema) multiplier) prev-ema)]
    (recur (drop 1 data-points) n multiplier (conj prev-emas ema))))))

(defn ema
  [periods]
  (let [data (:closes (bcc-api/get-all-data))]
    (calc-ema data periods)))

