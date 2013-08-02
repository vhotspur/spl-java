# Performance-based adaptation framework for Java

SPL for Java is a performance-based adaptation framework.
The purpose of this framework is to allow applications adapt their behaviour
based on their current or past performance.

## Requirements

To compile and run the framework, the following software has to be available
on your machine.

* Java SDK >= 1.7.0
* Apache Ant

## Compilation

The compilation is driven by Apache Ant, the target `agent` will build the
Java agent with the framework that you can attach to your application.

Executing target `test` will run the provided unit tests, the generated
report is in `out/test-results/html/index.html`.

## Documentation

... is not very detailed at the moment.

Running `ant refdoc` will generate JavaDoc documentation with basic information
on how to use SPL for Java
(into `out/javadoc/index.html`).

SPL was originally created for performance unit testing, more information
is available at [SPL for Java page](http://d3s.mff.cuni.cz/software/spl-java)
at [Department of Distributed and Dependable Systems](http://d3s.mff.cuni.cz/)
([Faculty of Mathematics and Physics, Charles University in Prague](http://mff.cuni.cz/)).

