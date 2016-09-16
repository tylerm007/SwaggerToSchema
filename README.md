# SwaggerToSchema
Use your swagger URL or local file and create to LAC Schema 

#Download this package
```
mvn install
```

#Download the swagger-parser package
```
mvn install
```

Copy the JAR file from SwaggerToSchema\target\SwaggerConverter.jar 
to the swagger-parser\target 

Copy the command file from SwaggerToSchema\genSchema.sh 
to swagger-parser

##To Run 
pass an argument for an swagger file or an http location

```
sh genSchema.sh http://petstore.swagger.io/v2/swagger.json
OR
sh genSchema.sh uber.json
```

The output is in CA Live API Creator @schema format - this can be used to create a new data model 
