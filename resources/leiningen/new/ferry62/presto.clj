(ns {{ name }}.presto
  (:require [hugsql.core :as hugsql]
            [mount.core :as mount]
            [clojure.core.cache :as cache]
            [clojure.java.jdbc :as jdbc]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]
            [clojure.string :as str]))

(conf/def presto-uri
  {:spec string?
   :default "//localhost:8080"})

(conf/def presto-user
  {:spec string?
   :default "root"})

(conf/def presto-password
  {:spec string?
   :secret true
   :default ""})

(conf/def presto-cache-size
    {:spec integer?
     :default 256})

(defonce cache  (atom  (cache/lru-cache-factory  {} :threshold presto-cache-size)))

(defn reset-cache  []
    (reset! cache  (cache/lru-cache-factory  {} :threshold presto-cache-size)))

(mount/defstate presto
  :start {:subprotocol "presto"
          :classname "com.facebook.presto.jdbc.PrestoDriver"
          :subname presto-uri
          :user presto-user
          :password presto-password}
  :stop :pass)

(defn- prepare-params [params]
  (log/info params)
  (for [i params]
    (condp = (type i)
      java.lang.String (str "'" (str/replace i "'" "''" "'"))
      (str i))))

(defn- fake-prepare-statement [sqlvec]
  (let [[sql & params] sqlvec
        format-sql (str/replace sql "?" "%s")]
    (apply format (conj (prepare-params params) format-sql))))

(defn- presto-query [sqlvec]
  (let [conn (jdbc/get-connection presto)
        statement (.createStatement conn)]
    (jdbc/metadata-result
     (.executeQuery statement (fake-prepare-statement sqlvec)))))

(defn query-cached [sqlvec]
  (cache/lookup (swap! cache
                       #(if  (cache/has? % sqlvec)
                          (cache/hit % sqlvec)
                          (cache/miss % sqlvec (presto-query sqlvec))))
                sqlvec))

(defn query [sqlvec]
  (presto-query sqlvec))

(hugsql/def-sqlvec-fns "queries/example.sql")
