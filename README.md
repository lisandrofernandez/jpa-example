# jpa-example

Simple example of how to use JPA.


## Requeriments

* Git to clone the repository.
* JDK 8 or superior.
* Apache Maven.


## Description

This example is intended to show the functionality of JPA API by implementing
CRUD operations.

Hibernate ORM is used as a JPA provider, and H2 database in memory mode, which
is created when the application connects to it and is destroyed when the
connection is closed.

In the database URL connection is included an initialization script to create
the tables of the database model and insert initial data. This is done
automatically when the connection is established. The path of the script is
``src/main/db/init.sql``. The file ``src/main/db/model.txt`` shows the model.


## Instructions

Clone the repository:

```bash
$ git clone https://github.com/lisandrofernandez/jpa-example.git
```

Get into the project root directory:

```bash
$ cd jpa-example
```

Open ``src/main/resources/META-INF/persistence.xml`` file and change the value
of the attribute ``value`` of the element ``property`` whose attribute ``name``
is ``javax.persistence.jdbc.url``, in order to point to the initialization
script located in the cloned repository. If, for example, the repository is in
user home directory, the element should be:

```xml
<property name="javax.persistence.jdbc.url"
          value="jdbc:h2:mem:test;INIT=runscript from '~/jpa-example/src/main/db/init.sql'" />
```

Compile with Maven:

```bash
$ mvn compile
```

Run with Maven:

```bash
$ mvn exec:java -Dexec.mainClass="org.lisandro.App"
```


## Copyright

Copyright &copy; 2018
[Lisandro Fernandez](https://dcc.fceia.unr.edu.ar/~lfernandez). See LICENSE.md
for details.
