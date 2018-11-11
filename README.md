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
Painless REST-API for a Hive storage.
Reduce the time to write boilerplate code on similar projects

## Usage

```
lein new ferry62 <NAME OF THE PROJECT>
```

OR

```
lein new ferry62 +swagger1st
```
in order to be able to manipulate the routing of the web application directly from the swagger definition.
This will also render `/ui` endpoint to enable the interactive UI documentation.

To start the application on the local environment, first you need to have some hive database ([this one](https://github.com/big-data-europe/docker-hive)
works pretty well for testing use-cases.

After that simply execute `lein run` inside of the projects to test your new application.


## Components

- Swagger definition (will be in `resources/{{ name }}.yaml` of your project)
- [Hugsql](https://github.com/layerware/hugsql) templates (your SQL queries) - <./resources/queries/example.sql>
- Configuration is done via environment:
    - `HIVE_URI`
    - `HIVE_USER`
    - `HIVE_PASSWORD`
    - `HTTP_HOST`
    - `HTTP_PORT`
    - `LOG_LEVEL`

## License

Copyright © 2018 Avdiushkin Vasilii

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
