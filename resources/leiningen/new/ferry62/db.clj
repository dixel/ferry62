(ns {{ name }}.db
  (:require [hugsql.core :as hugsql]
            [mount.core :as mount]
            [clojure.core.cache :as cache]
            [clojure.java.jdbc :as jdbc]
            [cyrus-config.core :as conf]))

(conf/def hive-uri
  "Hive storage URI (//host:port)"
  {:spec string?
   :default "//localhost:10000"})

(conf/def hive-user
  {:spec string?
   :default ""})

(conf/def hive-password
  {:spec string?
   :default ""})

(conf/def cache-size
    {:spec integer?
        :default 256})

(defonce cache  (atom  (cache/lru-cache-factory  {} :threshold cache-size)))

(defn reset-cache  []
    (reset! cache  (cache/lru-cache-factory  {} :threshold cache-size)))


(hugsql/def-sqlvec-fns "queries/example.sql")

(mount/defstate db
  :start {:subprotocol "hive2"
          :classname "org.apache.hive.jdbc.HiveDriver"
          :subname hive-uri
          :user hive-user
          :password hive-password}
  :stop :pass)

(defn query  [vec]
  (cache/lookup (swap! cache
                       #(if  (cache/has? % vec)
                          (cache/hit % vec)
                          (cache/miss % vec (jdbc/query db vec))))
                vec))
