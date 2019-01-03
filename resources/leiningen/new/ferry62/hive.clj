(ns {{ name }}.hive
  (:require [hugsql.core :as hugsql]
            [mount.core :as mount]
            [clojure.core.cache :as cache]
            [clojure.java.jdbc :as jdbc]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]))

(conf/def hive-uri
  "Hive storage URI (//host:port)"
  {:spec string?
   :default "//localhost:10000"})

(conf/def hive-user
  {:spec string?
   :default ""})

(conf/def hive-password
  {:spec string?
   :secret true
   :default ""})

(conf/def hive-cache-size
    {:spec integer?
     :default 256})

(defonce cache  (atom  (cache/lru-cache-factory  {} :threshold hive-cache-size)))

(defn reset-cache  []
    (reset! cache  (cache/lru-cache-factory  {} :threshold hive-cache-size)))

(mount/defstate hive
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
                          (cache/miss % vec (jdbc/query hive vec))))
                vec))

(hugsql/def-sqlvec-fns "queries/example.sql")
