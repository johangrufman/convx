Now
---
* Namespace urls

Later
-----
* Error handling
* Improve whitespace handling/leniency
* Namespaces
* Add proper character escaping (apache commons lang)
* Add corrupt flat file to acceptance tests
    - For example files without EOL
* Add corrupt flat file schemas
    - Simple errors such as illegal character sets
    - Grammar errors
* Add more writer unit tests
* XSL for documentation
* Get more real world examples
* Implement "simple type"-handling, i.e. dates, decimal numbers
* Finish hudson setup
* Refactor out xml-part of readers and writers
* Generalize "fields" in flat file schemas so that they can be declared locally and globally

Done
----
* Make sure the flat file parser implements the XMLEventReader contract
* Make sure the jar contains everything
* Add educational example
* Introduce character sets and use in new field node
* Improve the way character sets are defined in flat file schema (order between include/exclude etc)
* Add EOF-character
* Line
* Introduce ICU4J
    * UnicodeSet
* "Separated by"
* Quoting
* Add licence to pom
