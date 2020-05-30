Use the following commands to generate the classes:

`xjc -wsdl -mark-generated -d src/main/java -p ar.com.gtsoftware.service.afip.client.login src/main/resources/wsdl/login/*`
`xjc -wsdl -mark-generated -d src/main/java -p ar.com.gtsoftware.service.afip.client.fe src/main/resources/wsdl/fe/*`

Remember to set `namespace` attribute in package-info to avoid issues with xml formatting.
