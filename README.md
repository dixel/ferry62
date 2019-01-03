# ferry62

```
         O  O  O
                 O
                __|__
                || ||_____
   \____________|| || 62  |
    \               +-----+----+
     -------------------------/
      \   O   O   O   O      /
 ~ ~ ~ ~ ~ ~ ~ ~  ~ ~ ~ ~ ~ ~ ~ ~
  ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
```

## Goal
Easy-peasy data-driven CRUD application development.

```bash
lein new ferry62 beeeep +postgres +hive +presto
```

## Usage

In this example we'll use all 3 available data sources:

- Presto
- Hive
- Postgresql

This is rather an extreme scenario, you would rarely need to have this mixture in the real-world application, though it's nice for
demonstration purposes.

To start the application on the local environment, first you need to have some hive/presto or postgres database 
In case of Hive database ([this docker-compose](https://github.com/big-data-europe/docker-hive) works pretty well for local testing.
For Postgresql a docker-compose file which one can run via `docker-compose up -d` is available.

```bash
git clone https://github.com/big-data-europe/docker-hive
cd docker-hive
docker-compose up -d ## This will take quite a time to start all the components properly
cd ../
lein new ferry62 beeeep +postgres +hive +presto # will generate a project called `beeeep`
cd beeeep
docker-compose up -d # will start postgres docker container on localhost:5432
lein repl
```

```clojure
(start) ; will run the migrations in postgres (`./resources/migrations/001-example.up.sql`) and will initialize all the
database connectors and API components
; This way both REPL can be used for REPL-driven development:
(postgres/sample-fields-query postgres/postgres {:name "test" :date "2018-11-11" :age 25})
; => {:name "test", :age 25, :datem "2018-11-11"}
; In hive/presto sql vectors are used instead of rendered queries - this helps enabling caching/better flexibility
(hive/query (hive/sample-fields-query-sqlvec {:name "test" :date "2018-11-11" :age 25}))
; => ({:name "test", :age 25, :datem "2018-11-11"})
(presto/query (presto/sample-fields-query-sqlvec {:name "test" :date "2018-11-11" :age 25}))
; => ({:name "test", :age 25, :datem "2018-11-11"})
```

All those examples use a very basic SQL [query](./resources/leiningen/new/ferry62/example.sql) against the available storage backend:
New queries can be added by following [hugsql](https://www.hugsql.org/) template format.

Hive storage always works with LRU cache (because Hive is toooo slooow for the REST of us :))
The same query ran twice will return the same result.
To reset the hive cache, the following endpoint can be triggered: `curl http://localhost:4040/cache/reset`.
It can be used, for example, when you have daily batch jobs and want to reset the cache after the daily ETL is done.

Presto has a method which is not enabled by default, though still can be used:
```clojure
(time (presto/query (presto/sample-fields-query-sqlvec {:name "test" :date "2018-11-11" :age 25})))
```
```
19-01-03 20:54:23 host DEBUG [beeeep.presto:55] - presto query:  SELECT
'test'   AS name
,25   AS age
,'2018-11-11'  AS datem
"Elapsed time: 132.650789 msecs"
({:name "test", :age 25, :datem "2018-11-11"})
```

```clojure
(presto/query-cached (presto/sample-fields-query-sqlvec {:name "test" :date "2018-11-11" :age 25}))
(time (presto/query-cached (presto/sample-fields-query-sqlvec {:name "test" :date "2018-11-11" :age 25})))
```
```
"Elapsed time: 0.528328 msecs"
({:name "test", :age 25, :datem "2018-11-11"})
```

After starting, the following sample HTTP endpoints become available:

```bash
curl http://localhost:4040/sample?name=test&age=25&date=2018-11-11
curl http://localhost:4040/ping
```

## REPL
The following commands are available in `user` namespace when running the REPL:

*start all the components*
```
(start)
```

*stop all the components*
```
(stop)
```

*run raw queries against the local databases*
```
(presto-query "SELECT 1")
(hive-query "SELECT 1")
(postgres-query "SELECT 1")
```

## Components
- `src/../core.clj` - entrypoint: logging configuration, application startup for the deployment environment.
- `dev/user.clj` - entrypoint for `lein repl`
- `src/../api.clj` - API routes
- `src/../handlers.clj` - Functions called from the API - transforming API requests into the database queries, handling and processing the results.
- `src/../hive|postgres|presto.clj` - Boilerplate code/connection configuration for the databases
- `src/../repl.clj` - Configuration of the remote REPL (by default it start only on localhost)


## Configuration & building
For the development, the configuration will be stored in `.config.edn`.
For the deployment, you can build the application:

```bash
lein uberjar
```

Put it into docker
```bash
docker build .
```

And configure the application using environment variables on the deployment platform (kubernetes or whatever you use).

## Components

- [Hugsql](https://github.com/layerware/hugsql) templates (your SQL queries) - <./resources/queries/example.sql>
- [Timbre](https://github.com/ptaoussanis/timbre) is used for logging
- Configuration is done via environment variables and [cyrus-config](https://github.com/dryewo/cyrus-config) library and .config.edn file during the development

## License

Copyright © 2018 Avdiushkin Vasilii

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
