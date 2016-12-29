# EduGraph-Text-analysis

The ModulKatalogController is a webapplication which offers the functionality of analysing given modulcatalogs from universitys.
The Application is build and optimized for JBoss (wildfly) Application Server. The development is still in progress. 

It can be used in 3 different environments. The environments are configurated through properties files and the pom.xml from Maven.

1.) Environment: Development "dev". Everything has to be on localhost. 
* Wildfly 10.1.0.Final AS
* Solr Server 6.3.0 
* MongoDB 3.4.0 Community Edition
* local storage for catalog files
    
2.) Environment: University of Applied Science Brandenburg an der Havel "thb". Everything runs on a given Server from the university
* Wildfly 10.1.0.Final AS
* Solr Server 6.3.0
* MongoDB 3.4.0 Community Edition
* local storage for catalog files

3.) Environment: Amazon Web Services "aws". This option shows how the different services from AWS could be integrated in one application
* Wildfly 10.1.0.Final AS on a EC2 machine
* Solr Server 6.3.0 on an other EC2 machine
* DynamoDB as Services from AWS
* S3 Storage as upload directory for the catalog files
* invocation of an aws lambda function for calculating some scores (only for presenting how it works) 


# The development is still in progress.
