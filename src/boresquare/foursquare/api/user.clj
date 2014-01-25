(ns boresquare.foursquare.api.user
  (:refer-clojure :exclude [get])
  (:require [environ.core :as env]
            [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.string :refer [join]]
            [clj-time.core :refer [now]]
            [clj-time.format :refer [unparse, formatters]])
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

(def ^{:const true} current (unparse (formatters :basic-date) (now)))

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
    (str protocol "://" uri slash (if version (str version slash)) (join slash  (flatten segments)))))

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
    (str (create-uri *api-endpoint* segments) "?" "oauth_token=" (URLEncoder/encode (env/env :foursquare-token)) "&v=" current)))

(defn get
  ([^String uri]
    (io! (json/decode (:body @(http/get uri {:accept :json :throw-exceptions false})) true)))
  ([^String uri &{:as params}]
   (prn uri)
   (prn params)
    (io! (json/decode (:body @(http/get uri params)) true))))

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

(defn requests
  "Endpoint for the list of friend requests a user has"
  []
  (let [result (get (authenticated-uri "users" "requests"))]
    result))

(defn search
  "Locate friends with a bunch of parameters against the endpoint users/search

   Parameters:

      * :phone seq(string) - a list of phone numbers to search by.
      * :email seq(string) - a list of email addresses to search by.
      * :twitter seq(string) - a list of Twitter handles to search.
      * :twitterSource (string) - a single Twitter handle.
            Will return users that the handle follows on Twitter, who also use
      * :fbid (seq(string)) - A list of Facebook user IDs to search
      * :name (string): A name to search for
  "
  [& {:as params}]
  (let [result (get (authenticated-uri "users" "search") :query-params params)]
    result))

(defn checkins
  "Returns a history of checkins for a user.

  Parameters:

    * :limit (int) - number of results to return, up to 250 (default 100)
    * :offset (int) - number of results to return, up to 250 (default 100)
    * :sort (string) - sort the returned checkins, valid values are 'newestfirst' or 'oldestfirst' (default 'newestfirst')
    * :afterTimestamp - retrieve the first results that follow this value. The value is seconds after epoch.
    * :beforeTimestamp - retrieve the first results before this epoch time.
  "
  [& {:as params}]
  (let [result (get (authenticated-uri "users" "self" "checkins") :query-params params)]
    result))

(defn friends
  "Returns an array of a user's friends

  Parameters:

    * :limit (int) - number of results to return, up to 500
    * :offset (int) - Used to page through results.
  "
  [user & {:as params}]
  (let [result (get (authenticated-uri "users" user "friends") :query-params params)]
    result))

(defn mayorships
  "Returns a user's mayorships"
  [user]
  (let [result (get (authenticated-uri "users" user "mayorships"))]
    result))

(defn badges
  "Returns a user's badges"
  [user]
  (let [result (get (authenticated-uri "users" user "badges"))]
    result))

(defn lists
  "Returns lists associated with a user.

  Parameters:

    * :group (string) - Can be one of the following, 'created', 'edited', 'followed', 'friends', or 'suggested'
    * :ll (geographic coordinate) - Location of the user. Necessary if you specify :group 'suggested'
  "
  [user &{:as params}]
  (let [result (get (authenticated-uri "users" user "lists"))]
    result))

(defn photos
  "Returns photos a user has uploaded.

  Parameters:

    * :limit (int) - Number of results to return. Default 100, max 500.
    * :offset (int) - Used to page through results. Default 100.
  "
  [user &{:as params}]
  (let [result (get (authenticated-uri "users" user "photos"))]
    result))

(defn venuehistory
  "Returns a list of venues visited by a user, including count and last visit.

  Parameters:

    * :beforeTimestamp (int) - Seconds since epoch.
    * :afterTimestamp (int) - Seconds after epoch.
    * :categoryId (string) - Limits returned venues to this category. If a top-level category is specified, all sub-categories will match.
  "
  [user &{:as params}]
  (let [result (get (authenticated-uri "users" user "venuehistory"))]
    result))

(defn users-self
  "Returns information for the self user, hitting the endpoint:

  users/self
  "
  []
  (let [result (get (authenticated-uri "users" "self"))]
    (print "hello")
    result))
