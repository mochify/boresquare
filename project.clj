(defproject boresquare "0.1.0-SNAPSHOT"
  :description "Where were you on a given date?"
  :url "http://example.com/FIXME"
 :license {
    :name "Apache License, version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [enlive "1.1.5"]]
  :plugins [[lein-ring "0.8.8"]]
  :ring {:handler boresquare.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
