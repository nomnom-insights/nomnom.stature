(ns stature.metrics.protocol
  (:refer-clojure :exclude [count]))


(defprotocol Metrics

  (count
    [this key]
    "Increment a counter")

  (gauge
    [this key val]
    "Set gauge value")

  (timing
    [this key val]
    "Record timing"))


(defmacro with-timing
  "Nice macro to record timing of a given form."
  [statsd key & body]
  `(let [start-time# ^Long (System/currentTimeMillis)
         return# (do
                   ~@body)
         time# ^Long (- (System/currentTimeMillis) start-time#)]
     (timing ~statsd ~key time#)
     return#))


(defmacro count-on-exception
  "Evaluates the body and if an exception is thrown
   it increments a counter and re-throws it"
  [statsd key & body]
  `(try
     (do
       ~@body)
     (catch Exception err#
       (count ~statsd ~key)
       (throw err#))))
