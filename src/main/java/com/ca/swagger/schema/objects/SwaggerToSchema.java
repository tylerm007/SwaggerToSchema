package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;

public class SwaggerToSchema {

	private static final String RELN_PARENT_PREFIX = "PARENT_";
	private static final String RELN_CHILD_PREFIX = "CHILD_";
	public static int defaultStringSize = 20;

	public static void main(String[] args) {
		test();
		if (args.length > 0) {
			System.out.println(args[0]);
			System.out.println(generateSchema(args[0], true));
		}
		else {
			System.out.println("You must pass a Swagger 2.0 endpoint or a Swagger JSON file");
			System.out.println("SwaggerToSchema [dir/fileName | http:// endpoint].");
		}
	}

	private static void test() {
		//... does not work ....generateSchema("http://liveapi.dreamfactory.com/df-swagger-ui/dist/index.html",false);
		generateSchema("http://petstore.swagger.io/v2/swagger.json", true);
		//generateSchema("https://dev.expressologic.com/rest/default/demo_mysql/v1/@docs");
		//generateSchema("https://dev.expressologic.com/rest/default/demo/v1/@docs");
		//generateSchema("http://localhost:8080/APIServer/rest/default/banking/v1/@docs");
		//generateSchema("uber.json", true);
		//generateSchema("http://54.171.250.144/api-docs/customerManagementSwagger.json ",true);
		//generateSchema("//Users/banty01/swagger.json",true);
	}

	public static String generateSchema(String swaggerDoc) {
		return generateSchema(swaggerDoc, false);
	}

	public static String generateSchema(String swaggerDoc, boolean genericSwagger) {
		Map<String, String> lookupTable = new HashMap<String, String>();
		Schema schema = new Schema();
		Swagger swagger = new SwaggerParser().read(swaggerDoc);
		List<String> myPathList = getValidPathList(swagger);
		Map<String, Model> map = swagger.getDefinitions();
		for (String entityKey : map.keySet()) {
			Model m = map.get(entityKey);
			Map<String, Property> prop = m.getProperties();
			if (prop != null) {
				if (myPathList.contains(entityKey) || genericSwagger) {
					//System.out.println("Table: {\"entity\": " + entityKey + "}");
					Table table = new Table();
					table.setEntity(entityKey);
					schema.getTables().add(table);
					List<Attribute> columns = table.getColumns();
					for (String propKey : prop.keySet()) {
						Property p = prop.get(propKey);
						String type = getGenericType(p);
						Attribute attr = new Attribute();
						if (p.getType().equalsIgnoreCase("Array") || p.getType().equalsIgnoreCase("Ref")) {
							type = "string";
							if (propKey.equalsIgnoreCase(entityKey)) {
								propKey = entityKey;
							}
							//should be primary key of parent table
							lookupTable.put(propKey, entityKey);
						}
						else {
							//System.out.println("Attribute: {\"name\": " + propKey + ", generic_type: " + type + ", nullable: " + (!p.getRequired()) + ", size: 20}");
							attr.setName(propKey);
							attr.setGeneric_type(type);
							attr.setNullable((!p.getRequired()));
							attr.setSize(defaultStringSize);
							columns.add(attr);
						}
					}
					table.setColumns(columns);
				}
			}

		}

		for (String property : lookupTable.keySet()) {
			Relationship reln;
			Table table = new Table();
			table.setEntity(RELN_CHILD_PREFIX + property);

			Attribute attr = new Attribute();
			String origTable = lookupTable.get(property);
			//System.out.println("Table: {\"entity\": " + property + "}");
			//System.out.println("Attribute: {\"name\": " + property + ", generic_type: " + "string" + ", nullable: " + "false" + ", size: 20}");
			//System.out.println("Relationship to: " + origTable + " attribute[ " + property + "]");
			attr.setName(property + "_ident");
			attr.setGeneric_type("int");
			attr.setNullable(false);
			attr.setSize(defaultStringSize);
			table.getColumns().add(attr);
			schema.getTables().add(table);
			reln = new Relationship();
			reln.setRelationship_name("has_" + property);
			reln.setParent_entity(origTable);
			reln.addChild_column_names(property + "_ident");
			reln.addParent_column_names("ident");
			table.addParents(reln);
		}
		ObjectMapper mapper = new ObjectMapper();
		String outJson = null;
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			outJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema.getTables());
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			outJson = "{\"error\":\"" + e.getMessage() + "\"}";
		}
		System.out.println(outJson);
		return outJson;
	}

	private static String getGenericType(Property p) {
		String type = p.getType();
		String format = p.getFormat();
		if (format != null) {
			if (format.contains("int")) {
				type = "int";
			}
			else {
				if (format.contains("double")) {
					type = "decimal";
				}
				else {
					if (format.equals("date-time")) {
						type = "datetime";
					}
				}
			}
		}
		return type;
	}

	private static List<String> getValidPathList(Swagger swagger) {
		List<String> myPathList = new ArrayList<String>();
		List<Tag> myTags = swagger.getTags();
		if (myTags == null)
			return myPathList;
		Map<String, Path> paths = swagger.getPaths();
		for (Tag t : myTags) {
			for (String pathKey : paths.keySet()) {
				String tname = t.getName();
				Path p = paths.get(pathKey);
				if ((p.getGet() != null & p.getPost() != null) || pathKey.contains(tname)) {
					//System.out.println("Path " + pathKey);
					String key = pathKey.substring(1, pathKey.length());
					String[] split = (key + "/").split("/");
					if (!myPathList.contains(tname))
						myPathList.add(tname);
				}
			}
		}
		return myPathList;
	}
}
