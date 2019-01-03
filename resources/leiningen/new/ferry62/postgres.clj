(ns {{ name }}.postgres
  (:require [hugsql.core :as hugsql]
            [mount.core :as mount]
            [clojure.core.cache :as cache]
            [clojure.java.jdbc :as jdbc]
            [cyrus-config.core :as conf]
            [ragtime.jdbc :as rjdbc]
            [ragtime.jdbc.migrations :as migrations]
            [ragtime.repl :as ragtime-repl]
            [taoensso.timbre :as log]))

(conf/def pgsql-uri
  {:spec    string?
   :default "//localhost:5432/foobar"})

(conf/def pgsql-user
  {:spec    string?
   :default "postgres"})

(conf/def pgsql-password
  {:spec    string?
   :secret  true
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

(mount/defstate postgres
  :start (do
           (log/info "running migrations...")
           (migrate)
           (log/info "done with migrations")
           pgconfig)
  :stop :pass)

(defn query [vec]
  (jdbc/query postgres vec))

(hugsql/def-db-fns "queries/example.sql")
