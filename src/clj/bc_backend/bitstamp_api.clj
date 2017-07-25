(ns bc-backend.bitstamp-api
  (:require [clj-http.client :as http]
            [bc-backend.http-util :refer [json-get]]))

(def base-url "https://www.bitstamp.net/api")

(defn ticker
  []
  (json-get (str base-url "/ticker/")))

(defn ticker-hour
  []
  (json-get (str base-url "/ticker_hour/")))

(defn order-book
  []
  (json-get (str base-url "/order_book/")))

(defn transactions
  []
  (json-get (str base-url "/transactions/")))

(defn eur-usd
  []
  (json-get (str base-url "/eur_usd/")))
