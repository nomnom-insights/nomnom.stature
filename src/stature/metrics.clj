(ns stature.metrics
  (:refer-clojure :exclude [count])
  (:require [stature.metrics.protocol :as protocol]
            [com.stuartsierra.component :as component])
  (:import [com.timgroup.statsd NonBlockingStatsDClient]))

(def ^:final ^:private empty-tags (make-array String 0))

(defrecord MetricsComponent [host port prefix client]
  component/Lifecycle
  (start [c]
    (let [client (NonBlockingStatsDClient. prefix host port)]
      (assoc c :client client)))
  (stop [c]
    (when (:client c)
      (.stop ^NonBlockingStatsDClient client))
    (assoc c :client nil))

  protocol/Metrics
  (count [c key]
    (.incrementCounter ^NonBlockingStatsDClient client
                       ^String (name key)
                       empty-tags)
    key)
  (gauge [c key val]
    (.gauge ^NonBlockingStatsDClient client
            ^String (name key)
            ^double val
            empty-tags)
    val)
  (timing [c key val]
    (.recordExecutionTime ^NonBlockingStatsDClient client
                          ^String (name key)
                          ^double val
                          empty-tags)
    val))

(defn create
  "Create a new Statsd client. Options map:
  - host - String
  - port - Number
  - prefix - String"
  [opts]
  {:pre [(string? (:host opts))
         (number? (:port opts))
         (string? (:prefix opts))]}
  (map->MetricsComponent (merge opts {:client nil})))
