(defproject {{ name }} "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.1"]
                 {{#swagger1st}}
                 [org.zalando/swagger1st "0.25.0"]
                 {{/swagger1st}}
                 [cyrus/config "0.2.1"]
                 [mount "0.1.12"]
                 [com.layerware/hugsql "0.4.8"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.fzakaria/slf4j-timbre "0.3.8"]
                 [org.clojure/core.cache "0.7.1"]
                 [cheshire "5.8.1"]
                 {{#hive}}
                 [org.apache.hive/hive-jdbc "1.2.2"
                  :exclusions [[org.slf4j/slf4j-log4j12]
                               [org.apache.logging.log4j/log4j-slf4j-impl]
                               [log4j/log4j]]]
                 [org.apache.hadoop/hadoop-common "2.7.3"
                  :exclusions [[org.slf4j/slf4j-log4j12]
                               [org.apache.logging.log4j/log4j-slf4j-impl]
                               [log4j/log4j]]]
                 {{/hive}}
                 {{#nrepl}}
                 [nrepl/nrepl "0.5.3"]
                 {{/nrepl}}
                 {{#postgres}}
                 [org.postgresql/postgresql "42.2.5"]
                 [ragtime "0.7.2"]
                 {{/postgres}}
                 [mount "0.1.12"]
                 [aleph "0.4.4"]
                 [ring "1.6.3"]
                 [ring/ring-json "0.4.0"]]
  :main {{ name }}.core
  :profiles {:dev
             {:source-paths ["dev"]
              :repl-options {:init-ns user}
              :plugins
              [[lein-ancient "0.6.15"]
               [lein-kibit "0.1.5"]
               [jonase/eastwood "0.2.5"]]}})
