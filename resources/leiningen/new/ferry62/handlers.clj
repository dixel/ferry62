(ns {{ name }}.handlers
  (:require [ring.util.response :as r]
            [aleph.http :as http]
            [mount.core :as mount]
            [{{ name }}.db :as db]
            [cheshire.core :as json]
            [aleph.http :as http]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]
            [clojure.walk :refer [keywordize-keys]]))

(defn sample-fields
  "checking the connection and making hive return the echo payload"
  [request]
  (log/debugf "got request parameters: %s" (:query-params request))
  (let [db-vec (db/sample-fields-query-sqlvec (keywordize-keys (:query-params request)))
        db-result (db/query db-vec)]
    {:status 200
     :body db-result
     :headers {"Content-Type" "application/json"}}))

(defn reset-cache
  "reset the database cache"
  [request]
  (log/debugf "got request to flush the request cache...")
  {:status 200
   :body  (db/reset-cache)
   :headers  {"Content-Type" "application/json"}})
