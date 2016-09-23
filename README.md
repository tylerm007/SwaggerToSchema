# SwaggerToSchema
Use your swagger URL or a local file and create to LAC Schema file which can be used to POST and create data models.

##Clone this repository to a directory on your system.
```
git clone https://github.com/tylerm007/SwaggerToSchema.git ~/SwaggerToSchema

Change directory.

cd ~/SwaggerToSchema
```

##Install this package
```
mvn install
```


##To Run 
pass an argument for an swagger file or an http location

###Use a URL endpoint
```
sh genSchema.sh http://petstore.swagger.io/v2/swagger.json
```
###Pass a file in Swagger 2.0 format
```
sh genSchema.sh uber.json
```
###save the output to a file
```
sh genSchema.sh uber.json uberSchemaModel.json
```
##Command Line Schema Creation
The output is in CA Live API Creator @schema format - this can be used to create a new data model using the Live API Creator admin command line utility (NodeJS) - you can be a list of project idents (lacadmin project list) and you need to have a prefix of a managed data source (e.g. schema is editable) - (lacadmin datasource list).
```
lacadmin login -u [adminuser] -p [password] http://localhost:8080 -a schema
lacadmin use schema
lacadmin schema create --prefix [prefix] --project_ident [ident] --file uberSchemaModel.json
```
