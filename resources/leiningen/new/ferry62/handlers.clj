(ns {{ name }}.handlers
  (:require [ring.util.response :as r]
            [aleph.http :as http]
            [mount.core :as mount]
{{#hive}}
            [{{ name }}.hive :as hive]
{{/hive}}
{{#postgres}}
            [{{ name }}.postgres :as postgres]
{{/postgres}}
{{#presto}}
            [{{ name }}.presto :as presto]
{{/presto}}
            [cheshire.core :as json]
            [aleph.http :as http]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]
            [clojure.walk :refer [keywordize-keys]]))

{{#hive}}
(defn sample-fields
  "checking the connection and making hive return the echo payload"
{{#postgres}}
;; this function will be overriden by postgres handler
{{/postgres}}
{{#presto}}
;; this function will be overriden by presto handler
{{/presto}}
  [request]
  (log/debugf "got request parameters: %s" (:query-params request))
  (let [db-vec (hive/sample-fields-query-sqlvec (keywordize-keys (:query-params request)))
        db-result (hive/query db-vec)]
    {:status 200
     :body db-result
     :headers {"Content-Type" "application/json"}}))

(defn reset-cache
  "reset the database cache"
  [request]
  (log/debugf "got request to flush the request cache...")
  {:status 200
   :body  (hive/reset-cache)
   :headers  {"Content-Type" "application/json"}})
{{/hive}}

{{#presto}}
(defn sample-fields
  "checking the connection and making presto return the echo payload"
{{#postgres}}
;; this function will be overriden by postgres handler
{{/postgres}}
  [request]
  (log/debugf "got request parameters: %s" (:query-params request))
  (let [db-vec (presto/sample-fields-query-sqlvec (keywordize-keys (:query-params request)))
        db-result (presto/query db-vec)]
    {:status 200
     :body db-result
     :headers {"Content-Type" "application/json"}}))
{{/presto}}

{{#postgres}}
(defn sample-fields
  "checking the connection and making hive return the echo payload"
  [request]
  (log/debugf "got request parameters: %s" (:query-params request))
  (let [db-result (postgres/sample-fields-query postgres/postgres (keywordize-keys (:query-params request)))]
    {:status 200
     :body db-result
     :headers {"Content-Type" "application/json"}}))
{{/postgres}}

(defn pong
  [request]
  {:status 200
   :body {:result :pong}
   :headers {"Content-Type" "application/json"}})
