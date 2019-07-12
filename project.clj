(defproject nomnom/stature "2.0.0"
  :description "Componentized StatsD client for Clojure"
  :url "https://github.com/nomnom-insights/nomnom.stature"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.logging "0.5.0-alpha.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [com.datadoghq/java-dogstatsd-client "2.8"]]
  :deploy-repositories {"clojars" {:sign-releases false}}
  :min-lein-version "2.0.0"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :year 2018
            :key "mit"}
  :profiles {:dev
             {:dependencies [[aleph "0.4.6"]]}})
