# PolimiRESTServices

Expose data stored in a non-relational database by REST services.<br>
<table>
  <tr>
    <td><b>REST requests</b></td>
    <td>smart meter id and a period of time(two dates)</td>
    <td>district id and a period of time (two dates)</td>
  <tr> 
    <td><b>REST responses</b></td>
    <td>list of registers to build graph in <a href="https://github.com/pietroborrelli/smarth2oapp">smarth2o app</a>, avg water consumption and total consumption</td>
    <td>avg and total water consumption of neighborhood</td>
</table>
Requests and responses are JSON objects.

## Getting Started
Download Eclipse (v.>=4.7.3a) and install Java (v.>=1.8).
Install Spring Tools, in Eclipse Marketplace
```
Help -> Eclipse Marketplace -> Search 'Spring Tools' (v.>=3.9.4) -> Install and restart Eclipse
```
Import project in your workspace, then in Eclipse :
```
File -> Import -> Existing Project into Workspace
```
It'a a Maven Project, so each dependency will resolve automatically
```
Right click on project -> Maven -> Update Project
```

### Prerequisites

You need to have a working Cassandra database instance, populated with hourly registers. if you haven't, please follow before <a href="https://github.com/pietroborrelli/spring-data-cassandra" target="_blank" >this</a> guide.
<br>
Install Postman client to perform POSTs.

### Installing

In application.properties file:<br>
- set database connections <br>
- change eventually port on which Apache Tomcat listens
<br>
Have also a look in your build path, to check, in Libraries tab, that JRE is correctly set on a version >= 1.8

## Running 

Right click on the project and Run As Spring Boot App OR Run from Boot Dashboard (preferred)<br>
Use Postman client to perform REST calls and get responses.<br>
Here an example, http://localhost:8082/register/list 
```
Input
{ "meteringPointName":"CH_AQU_50991914",
  "startDate":"2017-03-20T00:00:00+0200",
  "endDate":"2017-03-20T23:00:00+0200"
}

Output
[ ...
    {
        "value": 0.076,
        "unixTimestamp": 1490022000000
    },
    {
        "value": 0.026,
        "unixTimestamp": 1490025600000
    },
    {
        "value": 0.104,
        "unixTimestamp": 1490029200000
    },
    {
        "value": 0.013,
        "unixTimestamp": 1490032800000
    },
 ...
]
```
For other requests inspect Controllers in web package.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Lombok](https://projectlombok.org/) - Easy access Java functions
* [Jackson](https://github.com/FasterXML/jackson/) - JSON Un/Marshaller

## Versioning

I use [SourceTree](https://www.sourcetreeapp.com/) as Git desktop client for versioning. But you may use your preferred  version control system.

## Authors

* **Pietro Borrelli** - *Initial work*

## Acknowledgments

* Thesis project - Politecnico di Milano

