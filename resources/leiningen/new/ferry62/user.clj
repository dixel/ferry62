(ns user
  (:require [mount.core :as mount]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]))

(log/merge-config! {:level :debug
                    :ns-whitelist ["{{ name }}.*"]})

(require '[{{ name }}.api :as api])
{{#hive}}
(require '[{{ name }}.hive :as hive])
{{/hive}}
{{#postgres}}
(require '[{{ name }}.postgres :as postgres])
{{/postgres}}
{{#nrepl}}
(require '[{{ name }}.repl :as repl])
{{/nrepl}}

(conf/reload-with-override! (read-string (slurp ".config.edn")))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))
