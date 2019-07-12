(ns stature.statsd-test
  (:require [clojure.test :refer :all]
            [stature.metrics.protocol :as protocol]
            [stature.metrics :as metrics]
            [stature.helper.statsd-server :as server]))

(def client (atom nil))
(def server (atom nil))
(def port 10029)

(def metric-store (atom nil))

(defn start!  []
  (reset! metric-store [])
  (reset! client (.start (metrics/create {:port port :host "127.0.0.1" :prefix "test"})))
  (reset! server (server/start port metric-store)))

(defn stop! []
  (swap! client #(.stop %))
  (swap! server server/stop)
  (reset! metric-store []))

(use-fixtures :each (fn [t]
                      (start!)
                      (t)
                      (stop!)))

(deftest sending-metrics
  (testing "sends all kinds of metrics"
    (is (= "counter" (protocol/count @client "counter")))
    (is (= 42 (protocol/gauge @client "gauge" 42)))
    (is (= 20 (protocol/gauge @client :meh.bar.foo 20)))
    (is (= 11 (protocol/with-timing @client "timing"
                (Thread/sleep 100)
                11)))
    ;; wait for things
    (Thread/sleep 500)
    (is (= [["test.counter" "1|c"]
            ["test.gauge" "42|g"]
            ["test.meh.bar.foo" "20|g"]]
           (take 3 @metric-store)))
    (let [[key value] (last @metric-store)]
      (is (= "test.timing" key))
      ;; this should have taken at least 100ms
      (is (re-find #"1..\|ms" value)))))

(deftest counting-exceptions
  (testing "'count-on-exception' macro incs a key on exception"
    (is (thrown? ArithmeticException
                 (protocol/count-on-exception
                  @client
                  :math.failure
                  (/ 7 0))))
    (is (= 10 (protocol/count-on-exception
               @client
               :math.failure
               (protocol/gauge @client :math.ok (/ 100 10)))))
    (Thread/sleep 500)
    (is (= [["test.math.failure" "1|c"]
            ["test.math.ok" "10|g"]]
           @metric-store))))
