# MetricsComponent

Metrics component can be used to record metrics in your application
and send recorded data to StatsD.

Component uses non-blocking StastD Client from [Java DogStatsD Client library](https://github.com/DataDog/java-dogstatsd-client)

> ⚠️ Stature doesn't support all of DogStatsD features, like [tagging, service checks](https://docs.datadoghq.com/developers/dogstatsd/data_types/) etc

### Component configuration

Following configuration is required when creating component

- **host:** host for StatsD UDP server
- **port:** port number for StatsD UDP server
- **prefix:** prefix to be used for all metric keys.
 Usually, it's the name of your application or the API key provided by hosted statsd providers like HostedGraphite.

### Metrics protocol

Metrics component implements two protocols

- component Lifecycle protocol ([Stuart Sierra's component lib](https://github.com/stuartsierra/component))
    - `start` method starts non-blocking StatsD client
    - `stop` method stops the non-blocking StatsD client
- stature Metrics protocol
    - `count` method increments counter for given key.
    - `gauge` method sets gauge value for given key.
    - `timing` method records timing for given key.

### Macros

Additionally following macros can be used with MetricsComponent:

- `with-timing` macro records timing for given form
- `count-on-exception` macro increments counter in case exception thrown during form evaluation
and it rethrows the exception

### Usage

```clojure

(require '[stature.metrics :as metrics]
         '[stature.protocol :as protocol]
         '[com.stuartsierra.component :as component])


(def metrics (-> (metrics/create {:host "statsd.internal" :port 8122 :prefix *ns*})
                 (component/start)))

(protocol/count metrics "foo.bar")
(protocol/gauge metrics "foo.baz" 42)

(protocol/with-timing metrics "some.timing"
  (do-expensive-work))

(metrics/count-on-exception "foo.bar.failure"
  (some-remote-call-that-fails)) ;; -> will increment foo.bar.failure counter if exception is thrown

```

### Mock component

For testing purposes or in development mode you can use Mock Component.
This will log the evaluated method instead sending data to StatsD.


```clojure
(require '[stature.metrics.mock]
         '[stature.metrics.protocol :as protocol])

(def m (mock/create))

(protocol/increment m "foo.bar")
```

### Simple NS (no direct component dependency)

You can use Stature without relying on Component with this wrapper namespace:


```clojure

(require '[stature.metrics :as metrics]
         '[stature.protocol :as protocol])

;; setup a client instance, but do not start it
(def client (atom (metrics/create {:host "127.0.0.1"
                                   :port 8125
                                   :prefix "stature"})))


;; helper fn to start and stop
(defn stop! []
  (reset! client (.stop @client)))

(defn start! []
  (reset! client (.start @client)))

(defn init! []
  (stop!)
  (start!))

;; define wrapper fn
;; client needs to be initialized in order to use fn below!

(defn count [key]
  (protocol/count @client key))

(defn gauge [key value]
  (protocol/gauge @client key value))

(defn timing [key value]
  (protocol/timing @client key value))

(defmacro with-timing [key & body]
  `(metrics/with-timing @client ~key ~@body))

(defmacro count-on-exception [key & body]
  `(metrics/count-on-exception @client ~key ~@body))

```