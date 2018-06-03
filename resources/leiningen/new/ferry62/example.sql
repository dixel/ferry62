-- :name sample-fields-query :? :1
-- :doc sample field set, call as (sample-fields-query db {:name_field "name" :age_field "age" :date_field "date"}) from the code
SELECT
  :name   AS name
  ,:age   AS age
  ,:date  AS datem
