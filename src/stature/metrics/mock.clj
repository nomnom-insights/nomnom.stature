(ns stature.metrics.mock
  (:require [stature.metrics.protocol :as protocol]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]))

(defrecord Mock []
  component/Lifecycle
  (start [this] this)
  (stop [this] this)
  protocol/Metrics
  (count [_this key]
    (log/debugf "%s incr" key))
  (gauge [_this key val]
    (log/debugf "%s g:%s" key val))
  (timing [_this key val]
    (log/debugf "%s t:%s" key val)))

(defn create []
  (->Mock))
