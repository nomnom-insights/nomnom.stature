(ns stature.helper.statsd-server
  (:require
    [aleph.udp :as udp]
    [byte-streams :as bs]
    [clojure.string :as str]
    [manifold.stream :as s]))


;; Adopted from aleph.udp examples
(defn parse-statsd-packet
  [{:keys [message]}]
  (let [message (bs/to-string message)]
    (->> message
         str/split-lines
         (map #(str/split %  #":")))))


(defn start
  [server-port data-store]
  (let [server-socket @(udp/socket {:port server-port})]
    (->> server-socket
         (s/map parse-statsd-packet)
         (s/consume (fn [metrics]
                      (mapv (fn [[metric value]]
                              (swap! data-store conj [metric value])) metrics))))
    server-socket))


(defn stop
  [sock]
  (s/close! sock))
