(ns stature.statsd-test
  (:require [clojure.test :refer :all]
            [stature.metrics.protocol :as protocol]
            [stature.metrics :as metrics]
            [stature.helper.statsd-server :as server]))

(deftest sending-metrics
  (testing "sends all kinds of metrics"
    (let [port 10029
          metrics (.start (metrics/create {:port port :host "127.0.0.1" :prefix "test"}))
          metric-store (atom [])
          statsd-server (server/start port metric-store)]

      (do
        (is (= "counter" (protocol/count metrics "counter")))
        (is (= 42 (protocol/gauge metrics "gauge" 42)))
        (is (= 20 (protocol/gauge metrics :meh.bar.foo 20)))

        (is (= 11 (metrics/with-timing metrics "timing"
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
          (is (re-find #"1..\|ms" value)))

        (server/stop statsd-server)
        (.stop metrics)))))

(deftest counting-exceptions
  (testing "'count-on-exception' macro incs a key on exception"
    (let [port 10029
          metrics (.start (metrics/create {:port port :host "127.0.0.1" :prefix "test-exceptions"}))
          metric-store (atom [])
          statsd-server (server/start port metric-store)]

      (do

        (is (thrown? ArithmeticException
                     (metrics/count-on-exception
                      metrics
                      :math.failure
                      (/ 7 0))))

        (is (= 10 (metrics/count-on-exception
                   metrics
                   :math.failure
                   (protocol/gauge metrics :math.ok (/ 100 10)))))

        (Thread/sleep 500)

        (is (= [["test-exceptions.math.failure" "1|c"]
                ["test-exceptions.math.ok" "10|g"]]
               @metric-store))

        (server/stop statsd-server)
        (.stop metrics)))))
