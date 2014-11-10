(ns boxes.main
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [goog.style :as style]
            [clojure.string :as string]
            [cljs.core.async :as async
             :refer [<! >! chan timeout]])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]))

(defn log [& params]
  (.log js/console (apply pr-str params)))

(defn get-rand-hex []
  (let [rand  (.random js/Math)
        min 0
        max 255]
    (.round js/Math (+ min (* rand (- max min))))))

(defn to-hex [num]
  (let [hex-num (.toString num 16)]
    (if (= (.-length hex-num) 1) (.concat "0" hex-num) hex-num)))

(defn put-line
  ([color] (put-line color "&nbsp;"))
  ([color chan-name]
     (let [n-p (dom/createElement "p" "colored")
           parent-node (dom/getElement "contents" )
           rand-color color]
       (set! (.-className n-p) "colored")
       (set! (.-innerHTML n-p) chan-name)
       (style/setStyle n-p "background-color" (js/String (str "#" rand-color)))
       (dom/appendChild parent-node n-p))))

(def chans {:c1 (chan) :c2 (chan) :c3  (chan)})

(go (while true
      (let [val (<! (get chans :c1))]
        (put-line val "&nbsp;"))))
(go (while true
      (let [val (<! (get chans :c2))]
        (put-line val "&nbsp;"))))
(go (while true
      (let [val (<! (get chans :c3))]
        (put-line val "&nbsp;"))))
(go
  (loop [n 200]
    (if (> n 0)
      (do
        (>! (get chans :c1) (string/join (map to-hex [(get-rand-hex) (get-rand-hex) (get-rand-hex)])))
        (<! (timeout 100))
        (recur (- n 1))))))
(go
  (loop [n 200]
    (if (> n 0)
      (do
        (>! (get chans :c2) (string/join (map to-hex [(get-rand-hex) (get-rand-hex) (get-rand-hex)])))
        (<! (timeout 500))
        (recur (- n 1))))))
(go
  (loop [n 200]
    (if (> n 0)
      (do
        (>! (get chans :c3) (string/join (map to-hex [(get-rand-hex) (get-rand-hex) (get-rand-hex)])))
        (<! timeout)
        (recur (- n 1))))))
