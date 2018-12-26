(ns {{ name }}.api-test
  (:require [{{ name }}.api :as sut]
            [clojure.test :refer :all]
			[clojure.core.cache :as cache]
{{#hive}}
            [{{ name }}.hive :as hive]
{{/hive}}
            ))

{{#hive}}
(deftest sample-fields-test
  (swap! hive/cache
         #(cache/miss
           %
           ["SELECT\n  ?   AS name\n  ,?   AS age\n  ,?  AS datem" "test" "123" "123"]
           {:name "test"
            :age "123" :datem "123"}))
  (is (= {:status 200
          :body {:name "test"
                 :age "123" :datem "123"}
          :headers {"Content-Type" "application/json"}}
         (sut/sample-fields {:query-params {:name "test"
                                            :age "123"
                                            :date "123"}}))))
{{/hive}}
