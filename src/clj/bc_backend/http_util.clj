(ns bc-backend.http-util
  (:require [clj-http.client :as http]))

(defn json-get
  [url]
  (let [response (http/get url 
                           { :as :json 
                            :cookie-policy :standard  ;workaround 
                            })
        body (:body response)]
    {:body body}))


