(ns {{ name }}.api-test
  (:require [{{ name }}.api :as sut]
            [clojure.test :refer :all]
            [{{ name }}.db :as db]))

(deftest sample-fields-test
  (binding [sut/sample-fields-query (fn [& args]
                                      :test-payload)]
    (is (= {:status 200
            :body {:result :test-payload}
            :headers {"Content-Type" "application/json"}}
           (sut/sample-fields {:query-params {}})))))
