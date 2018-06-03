(ns {{ name }}.db
  (:require [hugsql.core :as hugsql]
            [mount.core :as mount]
            [clojure.java.jdbc :as jdbc]
            [ragtime.repl :as ragtime]
            [clojure.spec.alpha :as spec]
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

(hugsql/def-db-fns "queries/example.sql")

(mount/defstate db
  :start {:subprotocol "hive2"
          :classname "org.apache.hive.jdbc.HiveDriver"
          :subname db-uri
          :user db-user
          :password db-password}
  :stop :pass)
