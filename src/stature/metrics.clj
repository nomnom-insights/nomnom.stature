(ns stature.metrics
  (:refer-clojure :exclude [count])
  (:require
    [com.stuartsierra.component :as component]
    [stature.metrics.protocol :as protocol])
  (:import
    (com.timgroup.statsd
      NonBlockingStatsDClient
      NonBlockingStatsDClientBuilder)))


(def ^:final ^:private empty-tags (make-array String 0))


(defrecord MetricsComponent
  [host port prefix client]

  component/Lifecycle

  (start
    [c]
    (let [^NonBlockingStatsDClient client (.. (NonBlockingStatsDClientBuilder.)
                                              (prefix prefix)
                                              (hostname host)
                                              (port port)
                                              (build))]
      (assoc c :client client)))


  (stop
    [c]
    (when (:client c)
      (.stop ^NonBlockingStatsDClient client))
    (assoc c :client nil))


  protocol/Metrics

  (count
    [_ key]
    (.incrementCounter ^NonBlockingStatsDClient client
                       ^String (name key)
                       empty-tags)
    key)


  (gauge
    [_ key val]
    (.gauge ^NonBlockingStatsDClient client
            ^String (name key)
            ^double val
            empty-tags)
    val)


  (timing
    [_ key val]
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
