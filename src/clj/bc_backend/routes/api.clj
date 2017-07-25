(ns bc-backend.routes.api
  (:require [bc-backend.layout :as layout]
            [bc-backend.bitstamp-api :as bs-api]
            [bc-backend.bitcoincharts-api :as bcc-api]
            [bc-backend.metrics :as metrics]
            [bc-backend.health-api :as health]
            [compojure.core :refer [defroutes context GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.data.json :as json]))

(def default-periods "14")

(defn get-periods 
  [req]
  (Integer/parseInt (get-in req [:params :periods] default-periods)))

(defroutes api-routes
  (context "/api" []
     (GET "/ticker" [] (bs-api/ticker))
     (GET "/ticker_hour" [] (bs-api/ticker-hour))
     (GET "/order_book" [] (bs-api/order-book))
     (GET "/transactions" [] (bs-api/transactions))
     (GET "/eur_usd" [] (bs-api/eur-usd))
     (GET "/historical-data" [] {:body (bcc-api/get-all-data)})
     (GET "/closes" [] {:body (bcc-api/closes)})
     (GET "/rsi" [] (fn [req] (json/write-str (metrics/rsi (get-periods req)))))
     (GET "/sma" [] (fn [req] (json/write-str (metrics/sma (get-periods req)))))
     (GET "/ema" [] (fn [req] (json/write-str (metrics/ema (get-periods req)))))
     (GET "/health" [] (health/health))))
