(ns stature.metrics.protocol)

(defprotocol Metrics
  (count [this key] "Increment a counter")
  (gauge [this key val] "Set gauge value")
  (timing [this key val] "Record timing"))