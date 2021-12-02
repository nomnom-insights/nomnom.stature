(defproject nomnom/stature "2.0.1"
  :description "Componentized StatsD client for Clojure"
  :url "https://github.com/nomnom-insights/nomnom.stature"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [com.stuartsierra/component "1.0.0"]
                 [com.datadoghq/java-dogstatsd-client "3.0.0"]]
  :deploy-repositories {"clojars" {:sign-releases false}}
  :min-lein-version "2.0.0"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :year 2018
            :key "mit"}
  :profiles {:dev
             {:dependencies [[aleph "0.4.6"]]}})
