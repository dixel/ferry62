(ns user
  (:require [mount.core :as mount]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]))

(log/merge-config! {:level :debug
                    :ns-whitelist ["{{ name }}.*"]})

(require '[{{ name }}.api :as api])
{{#db}}
(require '[{{ name }}.db :as db])
{{/db}}

(conf/reload-with-override! (read-string (slurp ".config.edn")))

(defn start []
  (mount/start [#'api/api])
{{#db}}
  (mount/start [#'db/db])
{{/db}}
  )

(defn stop []
  (mount/stop))
