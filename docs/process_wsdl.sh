# Download from https://repo1.maven.org/maven2/com/sun/xml/bind/jaxb-ri/
# Última version usada 2.3.4
xjc -wsdl -mark-generated -d src/main/java -p ar.com.gtsoftware.service.afip.client.fe src/main/resources/wsdl/fe/*