(ns boresquare.routes
  (:use compojure.core
        boresquare.views
        ring.adapter.jetty
        ring.middleware.reload
        ring.middleware.stacktrace)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.response :as response]
            ))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/listkeys" [] (listkeys))
  (GET "/user" [] "hello there")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))