(ns bc-backend.test.metrics
  (:require [clojure.test :refer :all]
            [bc-backend.metrics :refer :all]))

(def ma-closes [22.27 22.19 22.08 22.17 22.18 22.13 22.23 22.43 22.24 22.29 22.15 22.39 22.38 22.61 23.36 24.05 23.75 23.83 23.95 23.63 23.82 23.87 23.65 23.19 23.10 23.33 22.68 23.10 22.40 22.17])

(deftest test-sma-1
  (testing "sma"
    (let [ans (calc-sma ma-closes 10)
          expected [nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    22.220999999999997
                    22.209
                    22.229000000000003
                    22.259000000000004
                    22.303000000000004
                    22.421000000000003
                    22.613000000000007
                    22.765000000000004
                    22.905
                    23.076
                    23.209999999999997
                    23.377
                    23.525
                    23.651999999999997
                    23.71
                    23.684
                    23.612000000000002
                    23.505000000000003
                    23.432000000000002
                    23.276999999999997]]
      (is (= expected ans)))))

(deftest test-ema-1
  (testing "ema"
    (let [closes [11 12 13 14 15 16 17]
          ans (calc-ema closes 5)
          expected [nil nil nil nil 13 14.000000029802322 15.000000049670536]]
      (is (= expected ans)))))

(deftest test-ema-2
  (testing "ema"
    (let [ans (calc-ema ma-closes 10)
          expected [nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    nil
                    22.220999999999997
                    22.208090908706186
                    22.241165289927117
                    22.266407965238113
                    22.32887924614752
                    22.516355752435206
                    22.795200169393564
                    22.968800143768416
                    23.125381940477023
                    23.275312501222206
                    23.339801139285534
                    23.427110025653807
                    23.50763547793478
                    23.53351993726351
                    23.471061764990566
                    23.403595987527098
                    23.39021489848702
                    23.26108491309555
                    23.231796746205323
                    23.080560969660812
                    22.915004424788513]]
      (is (= expected ans)))))

(deftest test-average-1
  (testing "average"
    (let [closes [0.2 1 2.3 9 4.5 2 8]
          ans (average closes 3 0)]
      (is (= 1.1666666666666667 ans)))))

(deftest test-average-2
  (testing "average"
    (let [closes [0.2 1 2.3 9 4.5 2 8]
          ans (average closes 4 3)]
      (is (= 5.875 ans)))))


(def test-closes-1 [44.34 44.09 44.15 43.61 44.33 44.83 45.1 45.42 45.84 46.08 45.89 46.03 45.61 46.28 46.28 46.0 46.03 46.41])

(deftest test-get-rsis-1
  (testing "get-rsis"
    (let [closes test-closes-1
          rr (get-rsis closes 14)
          expected '(nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      nil
                      70.46413502109705
                      66.24961855355505
                      66.48094183471265
                      69.34685316290866) ]
      (is (= expected rr)))))

(deftest test-diffs-1 '(-0.25
                     0.05999999999999517
                     -0.5399999999999991
                     0.7199999999999989
                     0.5
                     0.2700000000000031
                     0.3200000000000003
                     0.4200000000000017
                     0.23999999999999488
                     -0.18999999999999773
                     0.14000000000000057
                     -0.4200000000000017
                     0.6700000000000017
                     0.0
                     -0.28000000000000114
                     0.030000000000001137
                     0.37999999999999545))

(deftest test-get-diffs-1
  (testing "get-diffs"
    (let [closes test-closes-1
          avgs (get-diffs closes)
          expected '(-0.25
                      0.05999999999999517
                      -0.5399999999999991
                      0.7199999999999989
                      0.5
                      0.2700000000000031
                      0.3200000000000003
                      0.4200000000000017
                      0.23999999999999488
                      -0.18999999999999773
                      0.14000000000000057
                      -0.4200000000000017
                      0.6700000000000017
                      0.0
                      -0.28000000000000114
                      0.030000000000001137
                      0.37999999999999545)]
      (is (= expected avgs)))))



(deftest test-get-avgs-1
  (testing "get-avgs"
    (let [ans (get-avgs [0 -1 -1 -1 -1 -1 ] 3)]
      (is (= {:avg-gains [0 0 0 0] 
              :avg-losses [2/3 7/9 23/27 73/81]} ans))))) 

(deftest test-get-avgs-2
  (testing "get-avgs"
    (let [ans (get-avgs [9 -1 -1 -1 -1 -1 ] 3)]
      (is (= {:avg-gains [3 2 4/3 8/9], :avg-losses [2/3 7/9 23/27 73/81]} ans))))) 
(deftest test-calc-avgs-1
  (testing "calc-avgs2"
    (let [ans (calc-avgs [1 1 1 1 1] 3 [1] [0] 3)]
      (is (= {:avg-gains [1 1 1], :avg-losses [0 0 0]} ans)))))

(deftest test-calc-avgs-2
  (testing "calc-avgs3"
    (let [diffs [1 0 1 -2 2 -4]
          ans (calc-avgs diffs 6 [0.24] [0.10])]
      (is (= {:avg-gains [0.24], :avg-losses [0.10]} ans)))))

(deftest test-calc-avgs-3
  (testing "calc-avgs3"
    (let [diffs [1 0 1 -2 2 -]
          ans (calc-avgs diffs 6 [0.24] [0.10])]
      (is (= {:avg-gains [0.24], :avg-losses [0.10]} ans)))))

(deftest test-initial-avgs
  (testing "initial-avgs"
    (let [all-diffs [0 -1 2 1 0 -3]
          avgs (initial-avgs all-diffs 6 5)
          avg-gain (first avgs)
          avg-loss (second avgs)]
      (is (= 1/2 avg-gain)
          (is (= 2/3 avg-loss))))))

(deftest test-loss-from-diff-1
  (testing "loss-from-diff"
    (let [loss (loss-from-diff 1)]
      (is (= 0 loss)))))

(deftest test-loss-from-diff-2
  (testing "loss-from-diff"
    (let [loss (loss-from-diff -1)]
      (is (= 1 loss)))))

(deftest test-gain-from-diff-1
  (testing "gain-from-diff"
    (let [gain (gain-from-diff 1)]
      (is (= 1 gain)))))

(deftest test-gain-from-diff-2
  (testing "gain-from-diff"
    (let [gain (gain-from-diff -1)]
      (is (= 0 gain)))))
