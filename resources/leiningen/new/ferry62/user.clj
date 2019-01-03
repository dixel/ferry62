(ns user
  (:require [mount.core :as mount]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]))

(log/merge-config! {:level :debug
                    :ns-whitelist ["{{ name }}.*"]})

(require '[{{ name }}.api :as api])
{{#hive}}
(require '[{{ name }}.hive :as hive])
(defn hive-query [query]
  ({{ name }}.hive/query [query]))
{{/hive}}
{{#postgres}}
(require '[{{ name }}.postgres :as postgres])
(defn postgres-query [query]
  ({{ name }}.postgres/query [query]))
{{/postgres}}
{{#presto}}
(require '[{{ name }}.presto :as presto])
(defn presto-query [query]
  ({{ name }}.presto/query [query]))
{{/presto}}
{{#nrepl}}
(require '[{{ name }}.repl :as repl])
{{/nrepl}}

(conf/reload-with-override! (read-string (slurp ".config.edn")))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))
