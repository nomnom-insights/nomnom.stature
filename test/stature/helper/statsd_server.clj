(ns stature.helper.statsd-server
  (:require  [aleph.udp :as udp]
             [byte-streams :as bs]
             [manifold.stream :as s]))

;; Adopted from aleph.udp examples
(defn parse-statsd-packet
  [{:keys [message]}]
  (let [message (bs/to-string message)
        [metric value] (clojure.string/split message #":")]
    [metric value]))

(defn start [server-port data-store]
  (let [server-socket @(udp/socket {:port server-port})]
    (->> server-socket
         (s/map parse-statsd-packet)
         (s/consume (fn [[metric value]]
                      (swap! data-store conj [metric value]))))
    server-socket))

(defn stop [sock]
  (s/close! sock))
