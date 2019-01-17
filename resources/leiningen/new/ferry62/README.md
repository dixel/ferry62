# {{ name }}
*REST-API for the data in {{#presto}}presto{{/presto}} {{#hive}}hive{{/hive}} {{#postgres}}postgres{{/postgres}}*

## Configuration
Configuration is generally done via environment variables.
For local development and REPL, `.config.edn` file can be used.
The following variables define the configuration of the application

```
"HTTP_PORT" 4040
"HTTP_HOST" "127.0.0.1"
{{#hive}}
"HIVE_URI" "//localhost:10000"
"HIVE_USER" "root"
"HIVE_PASSWORD" ""
"HIVE_CACHE_SIZE" 256
{{/hive}}
{{#postgres}}
"PGSQL_URI" "//localhost:5432/foobar"
"PGSQL_USER" "postgres"
"PGSQL_PASSWORD" "postgres"
{{/postgres}}
{{#presto}}
"PRESTO_URI" "//localhost:8080"
"PRESTO_USER" "root"
"PRESTO_PASSWORD" ""
"PRESTO_CACHE_SIZE" 256
"PRESTO_SSL" false
{{/presto}}
{{#nrepl}}
"NREPL_PORT" 7878
"NREPL_BIND" "127.0.0.1"
{{/nrepl}}
```

## Running {{ name }}
```bash
lein repl
```

```clojure
(start) ;; This project uses mount as a state manager. Mount enables reloading the components during the development.

; by default, the http://localhost:4040/ping is available

{{#db}}
;; running the queries to debug the DB connections
{{/db}}
{{#hive}}
(hive-query "SELECT 1")
{{/hive}}
{{#presto}}
(presto-query "SELECT 1")
{{/presto}}
{{#postgres}}
(postgres-query "SELECT 1")
{{/postgres}}
```

By default, database methods are defined in [](./resources/queries/example.sql) and loaded into the driver adapters namespaces.

```clojure
{{#hive}}
(hive/query (hive/sample-fields-query-sqlvec {:name "test" :age 25 :date "2018-11-11"}))
{{/hive}}
{{#presto}}
(presto/query-cached (presto/sample-fields-query-sqlvec {:name "test" :age 25 :date "2018-11-11"}))
{{/presto}}
{{#postgres}}
(postgres/query-cached (postgres/sample-fields-query-sqlvec {:name "test" :age 25 :date "2018-11-11"}))
{{/postgres}}
```

The endpoint that targets this database method is exposed here:
```bash
{{^swagger1st}}
curl http://localhost:4040/sample?name=test&age=25&date=2018-11-11
{{/swagger1st}}
{{#swagger1st}}
curl http://localhost:4040/api/sample?name=test&age=25&date=2018-11-11
{{/swagger1st}}
```

{{#swagger1st}}
## Swagger

Swagger UI is available [here](http://localhost:4040/ui/)

{{/swagger1st}}

## License
