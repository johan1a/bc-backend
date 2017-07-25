(ns bc-backend.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [bc-backend.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[bc-backend started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[bc-backend has shut down successfully]=-"))
   :middleware wrap-dev})
