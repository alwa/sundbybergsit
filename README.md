Kräver: TomEE Plus 1.0 (http://openejb.apache.org/apache-tomee.html)

Konfiguration för att köra med SQL Server:
Lägg till sqljdbc4.jar i $TomEE-Home$\lib
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