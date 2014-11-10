(ns boxes.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))
(defroutes app-routes
  ; to serve document root address
  (GET "/" [] "<p>Hello from compojure</p>")
  ; to serve static pages saved in resources/public directory
  (route/resources "/")
  ; if page is not found
  (route/not-found "Page not found"))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def handler
  (handler/site app-routes))
