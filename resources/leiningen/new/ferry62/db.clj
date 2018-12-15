(ns {{ name }}.db
  (:require [hugsql.core :as hugsql]
            [mount.core :as mount]
            [clojure.core.cache :as cache]
            [clojure.java.jdbc :as jdbc]
            [cyrus-config.core :as conf]
{{#postgres}}
            [ragtime.jdbc :as rjdbc]
            [ragtime.jdbc.migrations :as migrations]
            [ragtime.repl :as ragtime-repl]
{{/postgres}}
            [taoensso.timbre :as log]))

{{#hive}}
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

(conf/def cache-size
    {:spec integer?
        :default 256})

(defonce cache  (atom  (cache/lru-cache-factory  {} :threshold cache-size)))

(defn reset-cache  []
    (reset! cache  (cache/lru-cache-factory  {} :threshold cache-size)))

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
{{/hive}}

{{#postgres}}
(conf/def pgsql-uri
  {:spec    string?
   :default "//localhost:5432/foobar"})

(conf/def pgsql-user
  {:spec    string?
   :default "postgres"})

(conf/def pgsql-password
  {:spec    string?
   :default "postgres"})

(def pgconfig
  {:classname   "postgresql.Driver"
   :subprotocol "postgresql"
   :subname     pgsql-uri
   :user        pgsql-user
   :password    pgsql-password})

(def migration-config
  {:datastore  (rjdbc/sql-database pgconfig)
   :migrations  (rjdbc/load-resources "migrations")})

(defn migrate  []
    (ragtime-repl/migrate migration-config))

(mount/defstate db
  :start (do
           (log/info "running migrations...")
           (migrate)
           (log/info "done with migrations")
           pgconfig)
  :stop :pass)

(defn query [vec]
  (jdbc/query db vec))
{{/postgres}}

(hugsql/def-sqlvec-fns "queries/example.sql")
