(ns {{ name }}.handlers)

(defn sample-fields-transform [payload]
  (->> payload
       (assoc {} :result)))
