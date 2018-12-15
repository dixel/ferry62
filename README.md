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
Painless REST-API for a different storage systems.

## Usage

### storage = postgresql

```
lein new ferry62 <NAME OF THE PROJECT> +postgres
```

### storage = hive

```
lein new ferry62 <NAME OF THE PROJECT> +hive
```

### swagger1st support
*render HTTP routes purely based on swagger yaml definition*

```
lein new ferry62 <NAME OF THE PROJECT> +postgres +swagger1st
```

This will render `/ui` endpoint to enable the interactive UI documentation.

### sample

To start the application on the local environment, first you need to have some hive or postgres database 
In case of Hive database ([this one](https://github.com/big-data-europe/docker-hive) works pretty well for testing use-cases.
For Postgresql a docker-compose file which one can run via `docker-compose up -d` is available.

```bash
lein new ferry62 locomo +postgres
cd locomo
docker-compose up -d # will start postgres docker container on localhost:5432
lein repl
(start)
```

## Components

- [Hugsql](https://github.com/layerware/hugsql) templates (your SQL queries) - <./resources/queries/example.sql>
- Configuration is done via environment variables and [cyrus-config](https://github.com/dryewo/cyrus-config) library and .config.edn file during the development
- Swagger definition (will be in `resources/{{ name }}.yaml` of your project)

## License

Copyright © 2018 Avdiushkin Vasilii

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
