(ns {{ name}}.repl
  (:require [nrepl.server :as server]
            [cyrus-config.core :as conf]
            [mount.core :as mount]))

(conf/def nrepl-port
  {:spec integer?
   :default 7878})

(conf/def nrepl-bind
  {:spec string?
   :default "127.0.0.1"})

(mount/defstate repl
  :start (server/start-server :port nrepl-port :bind nrepl-bind)
  :stop (server/stop-server repl))
