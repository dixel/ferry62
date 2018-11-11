(ns leiningen.new.ferry62
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]
            [clojure.set :as sets]))

(def render (renderer "ferry62"))

(defn ferry62
  [name & args]
  (let [argset (into #{} (map keyword args))
        data {:name name
              :swagger1st (argset :+swagger1st)
              :plain (not (argset :+swagger1st))
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' ferry62 project.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["Dockerfile" (render "Dockerfile" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render ".gitignore")]
             [".config.edn" (render ".config.edn")]
             ["dev/user.clj" (render "user.clj" data)]
             ["src/{{sanitized}}/api.clj" (render "api.clj" data)]
             ["src/{{sanitized}}/db.clj" (render "db.clj" data)]
             ["src/{{sanitized}}/handlers.clj" (render "handlers.clj" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["test/{{sanitized}}/api_test.clj" (render "api_test.clj" data)]
             ["resources/queries/example.sql" (render "example.sql" data)]
             ["resources/{{sanitized}}-api.yaml" (render "swagger1st/ferry62-api.yaml" data)])))
