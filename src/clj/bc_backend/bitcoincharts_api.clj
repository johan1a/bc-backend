(ns bc-backend.bitcoincharts-api
  (:require [clj-http.client :as http]
            [bc-backend.http-util :refer [json-get]]
            [bc-backend.data-util :refer [parse-data parse-closes]]
            [hickory.core :as h]
            [hickory.select :as s]
            [clojure.string :as str]
            [clojure.data.json :as json]))

(def all-data-url
"https://bitcoincharts.com/charts/chart.json?m=bitstampUSD&SubmitButton=Draw&r=&i=&c=0&s=&e=&Prev=&Next=&t=S&b=&a1=&m1=10&a2=&m2=25&x=0&i1=&i2=&i3=&i4=&v=1&cv=0&ps=0&l=0&p=0&")

(defn get-url
  [url]
  (:body (http/get url)))

(defn to-hickory
  [raw-body]
  (h/as-hickory (h/parse raw-body)))

(defn get-remote-data
  []
  (let [response (to-hickory (get-url all-data-url))
        body-string (first (:content (first (s/select (s/tag :body) response))))
        body (json/read-str body-string)]
    body))


(defn get-all-data
  []
  (let [body (get-remote-data)]
  (parse-data body))) 

(defn closes
  []
  (let [body (get-remote-data)]
  (parse-closes body))) 
