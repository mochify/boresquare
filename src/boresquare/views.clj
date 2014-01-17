(ns boresquare.views
  (:require [net.cgrand.enlive-html :as html] :reload)
  (:require [environ.core :as env]))

(use '[clojure.string :only (join)])

(html/deftemplate listkeys "public/fskeys.html"
  []
  [:head :title] (html/content "Foursuare Key Info")
  [:body :h1] (html/content "Foursquare Keys")
  [:ul [:li html/first-of-type]] (html/content (join " " ["Client ID" (env/env :foursquare-client-id)])
))
