# stature

<img src="https://i.annihil.us/u/prod/marvel/i/mg/3/60/527413be6077d/standard_xlarge.jpg" align="right"  height="200px" />

Component friendly StatsD client for Clojure

[![CircleCI](https://circleci.com/gh/nomnom-insights/nomnom.stature.svg?style=svg)](https://circleci.com/gh/nomnom-insights/nomnom.stature)


## Usage

**Leiningen** (via [Clojars](https://clojars.org/nomnom/stature))

[![](https://img.shields.io/clojars/v/nomnom/stature.svg)](https://clojars.org/nomnom/stature)


**REPL**

```clojure
(require '[stature.metrics :as m]
         '[stature.protocol :as p])
```

## Metric component

See [here](doc/metrics.md)

## Release notes

#### v1.0.5 (12.9.2018)
Move tools.logging and component dependencies to main dependency list

### v1.0.4 (11.9.2018)
Add build status

### v1.0.3 (10.9.2018)
Fix deploy-repositories setting

### v1.0.2 (10.9.2018)
Open source nomnom/stature library
