(defproject nomnom/stature "1.0.5"
  :description "Componentized StatsD client for Clojure"
  :url "https://github.com/nomnom-insights/nomnom.stature"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.logging "0.4.1"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.datadoghq/java-dogstatsd-client "2.6.1"]]
  :deploy-repositories {"clojars" {:sign-releases false}}
  :min-lein-version "2.0.0"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :year 2018
            :key "mit"}
  :profiles {:dev
             {:dependencies [[aleph "0.4.6"]]}})
