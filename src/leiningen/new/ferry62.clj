(ns leiningen.new.ferry62
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "ferry62"))

(defn ferry62
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' ferry62 project.")
    (->files data
             ["project.clj"  (render "project.clj" data)]
             ["Dockerfile"  (render "Dockerfile" data)]
             ["README.md"  (render "README.md" data)]
             ["src/{{sanitized}}/api.clj"  (render "api.clj" data)]
             ["src/{{sanitized}}/db.clj"  (render "db.clj" data)]
             ["src/{{sanitized}}/core.clj"  (render "core.clj" data)]
             ["test/{{sanitized}}/api_test.clj" (render "api_test.clj" data)]
             ["resources/queries/example.sql" (render "example.sql" data)]
             ["resources/{{sanitized}}-api.yaml" (render "ferry62-api.yaml" data)])))
