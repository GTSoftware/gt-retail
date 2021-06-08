# Download from https://repo1.maven.org/maven2/com/sun/xml/bind/jaxb-ri/
# Última version usada 2.3.4
# El flag -npa agrega el namespace a cada objeto en lugar de colocarlo en el package-info
xjc -wsdl -npa -mark-generated -d src/main/java -p ar.com.gtsoftware.service.afip.client.fe src/main/resources/wsdl/fe/*