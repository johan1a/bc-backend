(ns bc-backend.pusher
  (import [com.pusher.client Pusher]
          [com.pusher.client.channel Channel SubscriptionEventListener])
  (:use clojure.pprint)
  (:require [clojure.data.json :as json]))

(def live-ticker 
  {:key "de504dc5763aeef9ff52"
   :channel "live_trades"
   :event "trade"})

(def live-order-book
  {:key "de504dc5763aeef9ff52"
   :channel "order_book"
   :event "data"})

(def live-full-order-book
  {:key "de504dc5763aeef9ff52"
   :channel "diff_order_book"
   :event "data"})

(def live-orders
  {:key "de504dc5763aeef9ff52"
   :channel "live_orders"
   :events ["order_created" 
            "order_changed" 
            "order_deleted"]})

(defn data-listener
  []
  (reify
    SubscriptionEventListener
    (onEvent [this channel-name event-name data]
      (clojure.pprint/pprint (json/read-str data))
      (println "---")
      )))

(defn listen
  [pusher-key channel-name event-name]
  (let [ pusher (Pusher. pusher-key)
        pusher-con (.connect pusher)
        channel (.subscribe pusher channel-name)
        b (.bind channel event-name (data-listener))]))

