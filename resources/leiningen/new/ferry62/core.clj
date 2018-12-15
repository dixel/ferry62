(ns {{ name }}.core
  (:gen-class)
  (:require [mount.core :as mount]
            [{{ name }}.api :refer [api]]
{{#nrepl}}
            [{{ name }}.repl :refer [repl]]
{{/nrepl}}
            [taoensso.timbre :as log]
            [cyrus-config.core :as conf]
            [clojure.string :as str]))

(conf/def log-level "log level WARN|INFO|ERROR|DEBUG"
  {:spec string?
   :default "INFO"})

(defn -main [& args]
  (log/merge-config! {:level (keyword (str/lower-case log-level))
                      :ns-whitelist ["{{ name }}.*"]})
  (log/info "starting the application...")
  (log/infof "configuration: \n %s" (conf/show))
  (mount/start)
  (log/info "application started...")
  (while true
    (Thread/sleep (Long/MAX_VALUE))))
