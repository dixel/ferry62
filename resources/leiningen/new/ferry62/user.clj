(ns user
  (:require [mount.core :as mount]
            [taoensso.timbre :as log]))

(log/merge-config! {:level :debug
                    :ns-whitelist ["{{ name }}.*"]})

(require '[{{ name }}.api :as api])
(require '[{{ name }}.db :as db])

(defn start []
  (mount/start [#'api/api #'db/db]))

(defn stop []
  (mount/stop))
