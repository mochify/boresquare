(defproject boresquare "0.1.0-SNAPSHOT"
  :description "Awesome Foursquare enhancements for you."
  :url "http://mochify.github.io/boresquare"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring "1.2.1"]
                 [enlive "1.1.5"]
                 [http-kit "2.1.16"]
                 [com.taoensso/carmine "2.4.0"]
                 [korma "0.3.0-RC6"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [info.cukes/cucumber-junit "1.1.5"]
                 [info.cukes/cucumber-clojure "1.1.5"]
                 [junit/junit "4.11"]
                 [environ "0.4.0"]
                 [lein-light-nrepl "0.0.10"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [clj-oauth "1.4.1"]
                 [cheshire "5.3.1"]]
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  :plugins [[lein-ring "0.8.8"]
            [lein-environ "0.4.0"]]
  :ring {:handler boresquare.routes/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
