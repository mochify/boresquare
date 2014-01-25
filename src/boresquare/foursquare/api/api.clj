(ns boresquare.api)

(defrecord FoursquareApiEndpoint
  [protocol url version])

(defn make-api-endpoint
  ([protocol host] (FoursquareApiEndpoint. protocol host nil))
  ([protocol host version] (FoursquareApiEndpoint. protocl host version)))

(def ^{:dynamic true} *api-endpoint* ())

(defn make-uri
  "Creates a Foursquare API URI from a FoursquareApiEndpoint"
  [^FoursquareApiEndpoint endpoint
   resource-path)

;(defmacro def-foursquare-endpoint
;  [verb resource-path & rest]
;  ())

;; API details to follow

;;(def-foursquare-endpoint :get "users")

;; User API has the form:
;; https://api.foursquare.com/v2/users/USER_ID
;;
;; Standard URL User API:
; users/leaderboard
; users/requests
; users/search
; users/USER_ID/badges
; users/USER_ID/checkins
; users/USER_ID/friends
; users/USER_ID/lists
; users/USER_ID/mayorships
; users/USER_ID/photos
; users/USER_ID/tips
; users/USER_ID/todos
; users/USER_ID/venuehistory
; users/USER_ID/approve
; users/USER_ID/deny
; users/USER_ID/setpings
; users/USER_ID/unfriend
; users/self/update ; updates a user's photo