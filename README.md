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

## Usage

```
lein new ferry62 highv
```


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
