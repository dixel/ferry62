(ns {{ name }}.api
  (:require [ring.util.response :as r]
            [aleph.http :as http]
            [mount.core :as mount]
            [io.sarnowski.swagger1st.core :as s1st]
            [{{ name }}.db :as db]
            [cheshire.core :as json]
            [aleph.http :as http]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]
            [clojure.walk :refer [keywordize-keys]]))

(conf/def http-port "http port of the app"
  {:spec integer?
   :default 4040})

(conf/def http-host "http host of the app"
  {:spec string?
   :default "127.0.0.1"})

(defn sample-fields
  "checking the connection and making hive return the echo payload"
  [request]
  (log/debugf "got request parameters: %s" (:query-params request))
  (let [db-vec (db/sample-fields-query-sqlvec (keywordize-keys (:query-params request)))
        db-result (db/query db-vec)]
    {:status 200
     :body db-result
     :headers {"Content-Type" "application/json"}}))

(mount/defstate api
  :start (do
           (log/info "starting the swagger-based API...")
           (http/start-server (-> (s1st/context :yaml-cp "{{ name }}-api.yaml")
                                  (s1st/discoverer)
                                  (s1st/mapper)
                                  (s1st/parser)
                                  (s1st/executor))
                              {:port http-port
                               :host http-host}))
  :stop (.close api))
