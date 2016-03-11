# Sundbybergs IT

## Beskrivning
Just nu så existerar bara en webapplikation: [Fatman](http://www.sundbybergsit.com)

## Byggstatus
[![Build Status](https://travis-ci.org/alwa/sundbybergsit.svg?branch=master)](https://travis-ci.org/alwa/sundbybergsit)

## Beroenden
- [TomEE Plus 1.7.2](http://openejb.apache.org/apache-tomee.html)
- [Maven 3.2.5+](https://maven.apache.org/)
- [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [JUnit 5](https://github.com/junit-team/junit5)
- MS SQL 2012+
- MSSQL JDBC driver (4.0)

## Setup
Lägg till sqljdbc4.jar i $TomEE-Home$\lib

<!-- OBSOLETE
Ändra $TomEE-Home$\conf\tomee.xml:

<Resource id="AccountDataSource" type="DataSource">
  JdbcDriver com.microsoft.sqlserver.jdbc.SQLServerDriver
  JdbcUrl jdbc:sqlserver://127.0.0.1:1433;DatabaseName=accounts;selectMethod=cursor;sendStringParametersAsUnicode=false
  UserName *********
  Password *********
  JtaManaged true
</Resource>


<Resource id="FatmanDataSource" type="DataSource">
  JdbcDriver com.microsoft.sqlserver.jdbc.SQLServerDriver
  JdbcUrl jdbc:sqlserver://127.0.0.1:1433;DatabaseName=fatman;selectMethod=cursor;sendStringParametersAsUnicode=false
  UserName *********
  Password *********
  JtaManaged true
</Resource>

-->