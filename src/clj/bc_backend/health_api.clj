(ns bc-backend.health-api
  (:require [clj-http.client :as http]))

(defn health
  []
  {:body {:message "OK"} :status 200})
