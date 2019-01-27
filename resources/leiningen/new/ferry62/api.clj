(ns {{ name }}.api
  (:require [{{ name }}.handlers :as handlers]
            [ring.util.response :as r]
{{#plain}}
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer  [wrap-params]]
{{/plain}}
            [aleph.http :as http]
            [mount.core :as mount]
{{#swagger1st}}
            [io.sarnowski.swagger1st.core :as s1st]
{{/swagger1st}}
{{#reitit}}
            [reitit.ring :as reitit]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as coercion]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
{{/reitit}}
{{#postgres}}
            [{{ name }}.postgres :as postgres]
{{/postgres}}
{{#presto}}
            [{{ name }}.presto :as presto]
{{/presto}}
{{#hive}}
            [{{ name }}.hive :as hive]
{{/hive}}
            [cheshire.core :as json]
            [aleph.http :as http]
            [cyrus-config.core :as conf]
            [taoensso.timbre :as log]))

(conf/def http-port "http port of the app"
  {:spec integer?
   :default 4040})

(conf/def http-host "http host of the app"
  {:spec string?
   :default "127.0.0.1"})

{{#plain}}
(defn app  [request]
    (log/debugf "request: %s"  (:uri request))
    (case  (:uri request)
{{#hive}}
          "/cache/reset"  (handlers/reset-cache request)
{{/hive}}
{{#db}}
          "/sample"  (handlers/sample-fields request)
{{/db}}
          "/ping" (handlers/pong request)
          {:status 400 :body (str "bad request: " (:uri request))}))
{{/plain}}

{{#reitit}}
(def app
  (reitit/ring-handler
    (reitit/router
      [["/swagger.json"
        {:get {:no-doc true
               :swagger {:info {:title "com.mytaxi.data.dallas API"
                                :description ""}}
               :handler (swagger/create-swagger-handler)}}]
       ["/sample" {:get {:parameters {:query {:name string?, :age int?, :date string?}}
                         :responses {200 {:body [{:name string?, :age string?, :datem string?}]}}
                         :handler handlers/sample-fields}}]
       ["/ping" {:get {:responses {200 {:body {:result "pong"}}}
                       :handler handlers/pong}}]]
      {:data {:coercion reitit.coercion.spec/coercion
              :muuntaja m/instance
              :middleware [parameters/parameters-middleware
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-response-middleware
                           exception/exception-middleware
                           muuntaja/format-request-middleware
                           coercion/coerce-response-middleware
                           coercion/coerce-request-middleware]}})
    (reitit/routes
      (swagger-ui/create-swagger-ui-handler
        {:path "/"
         :config {:validatorUrl nil}})
      (reitit/create-default-handler))))
{{/reitit}}

{{#swagger1st}}
(def app
  (-> (s1st/context :yaml-cp "{{ project }}-api.yaml")
      (s1st/discoverer)
      (s1st/mapper)
      (s1st/parser)
      (s1st/executor)))
{{/swagger1st}}

(mount/defstate api
  :start (do
           (log/info "starting the API component...")
           (http/start-server (-> app
                                {{#plain}}
                                  wrap-json-response
                                  wrap-params
                                {{/plain}}
                                )
                              {:port http-port
                               :host http-host}))
  :stop (.close api))
