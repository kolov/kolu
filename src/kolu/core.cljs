(ns kolu.core
  (:require [goog.net.XhrIo :as gxhr]
            [goog.dom :as dom]
            [goog.events :as events]
            [goog.ui.tree.TreeControl :as tree]
            [goog.ui.Component :as component]
            [clojure.string :as str]
            [clojure.browser.repl :as repl])
  (:use [jayq.util :only [map->js]])
  )

(defn json-generate
  "Returns a newline-terminated JSON string from ClojureScript data."
  [data]
  (str (JSON/stringify (clj->js data)) "\n"))

(defn json-parse
  "Returns ClojureScript data from a JSON string."
  [line]
  (js->clj (JSON/parse line)))

(defn query-update [q f set-status]
  "Query q and execute f on completion"
  (let [x (goog.net.XhrIo.)]
    (events/listen x (.-SUCCESS goog.net.EventType) #(f x))
    (events/listen x (.-ERROR goog.net.EventType) #(set-status "Network error. Try again later"))
    (.send x q)))

(defn append-div [parent clazz content]
  (let [child (dom/createDom "div" (map->js {"class" clazz}) content)]
    (dom/appendChild parent child) child))

(defn connect [] (repl/connect "http://localhost:9000/repl"))

(def jquery (js* "$"))
(defn clear-div [node] (dom/removeChildren node))

(def dom_ (dom/DomHelper.))