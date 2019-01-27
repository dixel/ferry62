(ns leiningen.new.ferry62
  (:require [leiningen.new.templates :refer [renderer
                                             name-to-path
                                             project-name
                                             ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "ferry62"))

(defn ferry62
  [name & args]
  (let [argset (into #{} (map keyword args))
        data {:name name
              :swagger1st (argset :+swagger1st)
              :reitit (argset :+reitit)
              :plain (not (argset :+swagger1st))
              :hive (argset :+hive)
              :postgres (argset :+postgres)
              :presto (argset :+presto)
              :db (or (argset :+postgres)
                      (argset :+hive)
                      (argset :+presto))
              :cached-db (or (argset :+hive)
                             (argset :+presto))
              :nrepl (argset :+nrepl)
              :sanitized (name-to-path name)
              :project (project-name (name-to-path name))
              }]
    (main/info (format "rendering new ferry62 project : [%s]" name))
    (apply (partial ->files data)
           (filter some?
                   [["project.clj" (render "project.clj" data)]
                    ["Dockerfile" (render "Dockerfile" data)]
                    ["README.md" (render "README.md" data)]
                    [".gitignore" (render "gitignore")]
                    [".config.edn" (render "config.edn" data)]
                    ["dev/user.clj" (render "user.clj" data)]
                    ["src/{{sanitized}}/api.clj" (render "api.clj" data)]
                    (when (:hive data)
                      ["src/{{sanitized}}/hive.clj" (render "hive.clj" data)])
                    (when (:postgres data)
                      ["src/{{sanitized}}/postgres.clj" (render "postgres.clj" data)])
                    (when (:presto data)
                      ["src/{{sanitized}}/presto.clj" (render "presto.clj" data)])
                    ["src/{{sanitized}}/handlers.clj" (render "handlers.clj" data)]
                    ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
                    (when (:nrepl data)
                      ["src/{{sanitized}}/repl.clj" (render "repl.clj" data)])
                    ["test/{{sanitized}}/api_test.clj" (render "api_test.clj" data)]
                    (when (:db data)
                      ["resources/queries/example.sql" (render "example.sql" data)])
                    (when (:postgres data)
                      ["docker-compose.yaml" (render "docker-compose.yaml")])
                    (when (:postgres data)
                      ["resources/migrations/001-example.up.sql" (render "001-example.up.sql" data)])
                    (when (:swagger1st data)
                      ["resources/{{ project }}-api.yaml" (render "swagger1st/ferry62-api.yaml" data)])]))))
