(defproject boresquare "0.1.0-SNAPSHOT"
  :description "Awesome Foursquare enhancements for you."
  :url "http://mochify.github.io/boresquare"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring "1.2.1"]
                 [enlive "1.1.5"]
                 [http-kit "2.1.13"]]
  :plugins [[lein-ring "0.8.8"]]
  :ring {:handler boresquare.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
