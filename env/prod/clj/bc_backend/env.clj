(ns bc-backend.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[bc-backend started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[bc-backend has shut down successfully]=-"))
   :middleware identity})
