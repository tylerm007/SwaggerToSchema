#!/bin/bash
export EL_SETUP=target
export EL_JAVA=${EL_SETUP}/lib
export EL_CLASSPATH=\
${EL_SETUP}:\
lib/SwaggerConverter.jar:\
${EL_JAVA}/ant-1.7.0.jar:\
${EL_JAVA}/ant-launcher-1.7.0.jar:\
${EL_JAVA}/aopalliance-1.0.jar:\
${EL_JAVA}/bsh-2.0b4.jar:\
${EL_JAVA}/cal10n-api-0.7.4.jar:\
${EL_JAVA}/commons-codec-1.9.jar:\
${EL_JAVA}/commons-io-2.4.jar:\
${EL_JAVA}/commons-lang-2.6.jar:\
${EL_JAVA}/commons-lang3-3.2.1.jar:\
${EL_JAVA}/commons-logging-1.2.jar:\
${EL_JAVA}/guava-18.0.jar:\
${EL_JAVA}/guice-4.0-no_aop.jar:\
${EL_JAVA}/httpclient-4.5.jar:\
${EL_JAVA}/httpcore-4.4.1.jar:\
${EL_JAVA}/jackson-annotations-2.4.5.jar:\
${EL_JAVA}/jackson-core-2.6.0.jar:\
${EL_JAVA}/jackson-databind-2.4.5.jar:\
${EL_JAVA}/jackson-dataformat-yaml-2.4.5.jar:\
${EL_JAVA}/jackson-datatype-joda-2.4.5.jar:\
${EL_JAVA}/javax.inject-1.jar:\
${EL_JAVA}/jcommander-1.48.jar:\
${EL_JAVA}/jetty-6.1.26.jar:\
${EL_JAVA}/jetty-util-6.1.26.jar:\
${EL_JAVA}/jmockit-1.19.jar:\
${EL_JAVA}/joda-time-2.2.jar:\
${EL_JAVA}/jopt-simple-4.9.jar:\
${EL_JAVA}/json-20090211.jar:\
${EL_JAVA}/json-path-0.8.1.jar:\
${EL_JAVA}/json-smart-1.1.1.jar:\
${EL_JAVA}/jsonassert-1.2.3.jar:\
${EL_JAVA}/servlet-api-2.5-20081211.jar:\
${EL_JAVA}/slf4j-api-1.6.3.jar:\
${EL_JAVA}/slf4j-ext-1.6.3.jar:\
${EL_JAVA}/snakeyaml-1.15.jar:\
${EL_JAVA}/swagger-annotations-1.5.8.jar:\
${EL_JAVA}/swagger-core-1.5.8-tests.jar:\
${EL_JAVA}/swagger-core-1.5.8.jar:\
${EL_JAVA}/swagger-models-1.5.8.jar:\
${EL_JAVA}/testng-6.9.6.jar:\
${EL_JAVA}/validation-api-1.1.0.Final.jar:\
${EL_JAVA}/wiremock-1.57.jar:\
${EL_JAVA}/xmlunit-1.6.jar:\
target/swagger-parser-1.0.23-SNAPSHOT.jar
echo ${EL_CLASSPATH}
//java -cp ${EL_CLASSPATH} com.ca.swagger.schema.objects.SwaggerToSchema /Users/banty01/Documents/workspace/SwaggerParser/uber.json
java -cp ${EL_CLASSPATH} com.ca.swagger.schema.objects.SwaggerToSchema http://petstore.swagger.io/v2/swagger.json