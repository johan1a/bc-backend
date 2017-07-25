(ns user
  (:require [mount.core :as mount]
            bc-backend.core))

(defn start []
  (mount/start-without #'bc-backend.core/repl-server))

(defn stop []
  (mount/stop-except #'bc-backend.core/repl-server))

(defn restart []
  (stop)
  (start))


