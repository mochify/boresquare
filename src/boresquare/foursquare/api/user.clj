(ns boresquare.foursquare.api.user
  (:refer-clojure :exclude [get])
  (:require [environ.core :as env]
            [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.string :refer [join]])
  (import [java.net URLEncoder]))

;; This wraps the Foursquare User API
;; https://developer.foursquare.com/docs/users/users

;; User API has the form:
;; https://api.foursquare.com/v2/users/USER_ID
;;
;; Standard URL User API:
;; users/USER_ID
;; users/leaderboard
;; users/requests
;; users/search
;; users/USER_ID/badges
;; users/USER_ID/checkins
;; users/USER_ID/friends
;; users/USER_ID/lists
;; users/USER_ID/mayorships
;; users/USER_ID/photos
;; users/USER_ID/tips
;; users/USER_ID/todos
;; users/USER_ID/venuehistory
;; users/USER_ID/approve
;; users/USER_ID/deny
;; users/USER_ID/setpings
;; users/USER_ID/unfriend
;; users/self/update ; updates a user's photo


(defrecord FoursquareApiEndpoint
  [protocol uri version])

(def ^{:const true} current (.format (java.text.SimpleDateFormat. "YYYYMMDD") (java.util.Date.)))

(def ^{:const true} slash "/")
(def ^{:const true} encoding "UTF-8")

(defn make-api-endpoint
  ([protocol uri] (FoursquareApiEndpoint. protocol uri nil))
  ([protocol uri version] (FoursquareApiEndpoint. protocol uri version)))

(def ^{:dynamic true} *api-endpoint* (make-api-endpoint "https" "api.foursquare.com" "v2"))

(defn create-uri
  [^FoursquareApiEndpoint endpoint
   & segments]
  (let [protocol (:protocol endpoint)
        uri (:uri endpoint)
        version (:version endpoint)]
    (str protocol "://" uri "/" (if version (str version "/")) (join "/"  (flatten segments)))))

(defn userless-uri
  [& segments]
  (let
    [date current]
    (str (create-uri *api-endpoint* segments)
         "?" "client_id=" (env/env :foursquare-client-id)
         "&" "client_secret=" (env/env :foursquare-client-secret)
         "&" "v=" current)))

(defn authenticated-uri
  [& segments]
  (let
    [date current]
    (str (create-uri *api-endpoint* segments) "?" "oauth_token=" (URLEncoder/encode (env/env :foursquare-token)) "&" "v=" current)))

(defn get
  [^String uri]
  (io! (json/decode (:body @(http/get uri {:accept :json :throw-exceptions false})) true)))

(defn make-uri
  "Creates a Foursquare API URI from a FoursquareApiEndpoint"
  [^FoursquareApiEndpoint endpoint
   resource-path]
  (= 1 1))

(defn leaderboard
  "hits the leaderboard endpoint"
  []
  (let [result (get (authenticated-uri "users" "leaderboard"))]
    result))

(defn users-self
  "Returns information for the self user, hitting the endpoint:

  users/self
  "
  []
  (let [result (get (authenticated-uri "users" "self"))]
    result))