# :warning: This library is no longer maintained

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
         '[stature.netrics.protocol :as p])
```

## Metric component

See [here](doc/readme.md)

## Release notes


#### v2.0.1 2021-12-02

Updated depdencies & migrated CI to GitHub Actions.

#### v2.0.0 2019-07-12

No new features.

Reorganized `stature.metrics` namespace and moved utility macros to `stature.metrics.protocol` to
make it easier to use the component.

Updated dependencies.

#### v1.0.5 2018-09-12

Moved tools.logging and component dependencies to main dependency list

### v1.0.4 2018-09-11

Added build status

### v1.0.3 2018-09-10

Fixed deploy-repositories setting

### v1.0.2 2018-09-10

Open sourced nomnom/stature library
