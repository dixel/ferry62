# Changelog

## [0.0.13]
- Add support for PrestoDB (old versions). Lack of prepared statements there had to be compensated not very cleanly

## [0.0.12]
- Support multiple databases in one project (for example for migration scripts)

## [0.0.11]
### Added
- Leiningen 2.8.3 support
- Choose between postgres/hive with +postgres/+hive options
- Nrepl support (+nrepl)

### Removed
- Ultra plugin (leiningen compatibility)

## [0.0.4]
### Added
- Ability to choose between plain ring (default) and swagger1st.

## [0.0.3]
### Added
- Swagger1st (read yaml swagger definition, pass clojure functions as handlers)

## [0.0.2]
### Added
- Library dependencies (hive, logging, configuration, mount)
- Wiring the application, startup modules
