(ns {{ name }}.api-test
  (:require [{{ name }}.api :as sut]
            [clojure.test :refer :all]
			[clojure.core.cache :as cache]
{{#db}}
            [{{ name }}.db :as db]
{{/db}}
            ))

{{#db}}
(deftest sample-fields-test
  (swap! db/cache
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
{{/db}}
