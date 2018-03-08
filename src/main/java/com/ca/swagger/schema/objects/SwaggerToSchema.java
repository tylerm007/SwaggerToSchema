package com.ca.swagger.schema.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;

public class SwaggerToSchema {

	private static final String RELN_CHILD_PREFIX = "CHILD_";
	private static final String RELN_PARENT_PREFIX = "PARENT_";
	public static int defaultStringSize = 20;

	public static void main(String[] args) {
		SwaggerToSchema s2schema = new SwaggerToSchema();

		String fileName = null;
		if (args.length >= 1) {
			if (args.length >= 2) {
				//System.out.println("arg0 "+args[0]);
				//System.out.println("arg1 "+args[1]);
				fileName = args[1];
			}
			try {
				String contents = s2schema.generateSchema(args[0], true);
				s2schema.writeToFile(fileName, contents);
				//System.out.println("arg0 "+args[0]);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("You must pass a Swagger 2.0 endpoint or a Swagger JSON file and optional fileName");
			System.out.println("SwaggerToSchema [dir/fileName | http:// endpoint] [directory/filename.json]");
		}
	}

	public String generateSchema(String swaggerDoc) {
		return generateSchema(swaggerDoc, false);
	}

	public String generateSchema(String swaggerDoc, boolean genericSwagger) {
		List<LookupTable> lookupTable = new ArrayList<>();
		Schema schema = new Schema();
		Swagger swagger = new SwaggerParser().read(swaggerDoc);
		List<String> myPathList = getValidPathList(swagger);
		Map<String, Model> map = swagger.getDefinitions();
		for (String entityName : map.keySet()) {
			Model m = map.get(entityName);
			String entityKey = findBaseTable(entityName);
			Map<String, Property> prop = m.getProperties();
			if (prop != null) {
				if (myPathList.contains(entityKey) || genericSwagger) {
					Table table = new Table();
					Key key = new Key();
					table.addKey(key);
					key.addColumn("ident");
					table.setEntity(entityKey);
					
					List<Attribute> columns = table.getColumns();
					String subtype = null;
					for (String propKey : prop.keySet()) {
						Property p = prop.get(propKey);
						String type = getGenericType(p);
						Attribute attr = new Attribute();
						if (p.getType().equalsIgnoreCase("Array") || p.getType().equalsIgnoreCase("Ref")) {
							type = "number";
							//should be primary key of parent table
							String refEntity = propKey;
							if (p.getType().equalsIgnoreCase("Ref")) {
								if (p instanceof RefProperty) {
									refEntity = ((RefProperty)p).getSimpleRef();
								}
							}
							LookupTable lookup = new LookupTable(entityKey, refEntity, propKey);
							lookup.setRefArray(p.getType());
							lookupTable.add(lookup);
						}
						else {
							attr.setName(propKey);
							attr.setGeneric_type(type);
							attr.setNullable((!p.getRequired()));
							if (type.equals("string"))
								attr.setSize(defaultStringSize);
							subtype = genSubType(type);
							if (subtype != null) {
								attr.setSubtype(subtype);
							}
							if(columns.isEmpty()) {
								columns.add(createIdentColumn());
							}
							if (!table.hasAttr(attr) && !p.getType().equalsIgnoreCase("object")){
								columns.add(attr);
							}
						}
					}

					table.setColumns(columns);
					table.addPrimaryKeyColumns("ident");
					if(!columns.isEmpty() && !schema.hasTableName(table.getEntity())) {
						schema.getTables().add(table);
					}
				}
			}
		}

		for (LookupTable lookup : lookupTable) {
			Relationship reln;
			Table childTable;
			Table parentTable;
			List<Table> listOfTables = schema.getTables();
			//ref type is a parent pick - Array is a child list
			boolean isRefType = lookup.getRefArray().equalsIgnoreCase("Ref");
			String baseEntity = lookup.getBaseEntity();
			String refEntity = lookup.getRefEntity();
			String parentTableName;
			String columnName = lookup.getColumnName();
			if (isRefType) {
				childTable = findTable(listOfTables, baseEntity);
				parentTable = findTable(listOfTables, refEntity);
				parentTableName = findBaseTable(refEntity);
			}
			else {
				childTable = findTable(listOfTables, refEntity);
				parentTable = findTable(listOfTables, baseEntity);
				parentTableName = baseEntity;
			}

			if (childTable == null && !schema.hasTableName(RELN_CHILD_PREFIX + columnName)) {
				childTable = new Table();
				schema.getTables().add(childTable);
				Key key = new Key();
				childTable.addKey(key);
				key.addColumn("ident");
				childTable.addPrimaryKeyColumns("ident");
				childTable.getColumns().add(createIdentColumn());
				childTable.setEntity(RELN_CHILD_PREFIX + columnName);
			} else {
				if(schema.hasTableName(RELN_CHILD_PREFIX + columnName)){
					for(Table t :schema.getTables()){
						if(t.getEntity().equals(RELN_CHILD_PREFIX + columnName)){
							childTable = t;
							break;
						}
					}
				}
			}
			if (isRefType) {
				//add the attribute t the table
				Attribute attr = new Attribute();
				attr.setName(columnName);
				attr.setGeneric_type("number");
				attr.setSubtype("integer");
				attr.setNullable(false);
				//attr.setSize(20);
				if (!childTable.hasAttr(attr)){
					childTable.getColumns().add(attr);
				}
			}
			//create the relationship to the child
			reln = new Relationship();
			reln.setRelationship_name("has_" + columnName);
			reln.setParent_entity(parentTableName);
			reln.addChild_column_names(columnName);
			reln.addParent_column_names("ident");
			if(!childTable.hasExistingReln(reln)){
				childTable.addParents(reln);
			}

		}
		ObjectMapper mapper = new ObjectMapper();
		String outJson = null;
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			outJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema.getTables());
		}
		catch (Exception e) {
			e.printStackTrace();
			outJson = "{\"error\":\"" + e.getMessage() + "\"}";
		}
		//System.out.println(outJson);
		return outJson;
	}

	private String findBaseTable(String entityKey) {
		if(entityKey.contains(".")){
			String[] key = entityKey.split("\\.");
			return key[key.length - 1];
		}
		if(entityKey.contains(":")){
			String[] key = entityKey.split("\\:");
			return key[key.length - 1];
		}
		return entityKey;
	}

	private Table findTable(List<Table> tables, String tableName) {
		Table table = null;
		for (Table atable : tables) {
			if (atable.getEntity().equalsIgnoreCase(tableName)) {
				table = atable;
				break;
			}
		}
		return table;
	}

	private String genSubType(String type) {
		if ("number".equals(type))
			return "integer";
		return null;
	}

	private String getGenericType(Property p) {
		String type = p.getType();
		String format = p.getFormat();
		if (format != null) {
			if (format.contains("int")) {
				type = "number";
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

	private List<String> getValidPathList(Swagger swagger) {
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
					if (!myPathList.contains(tname))
						myPathList.add(tname);
				}
			}
		}
		return myPathList;
	}

	public void writeToFile(String fileName, String content) throws Exception {
		//TODO
		if (fileName == null) {
			//System.out.println(content);
		}
		else {
			//write to fileName
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.canWrite()) {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
				System.out.println("Schema file written to: " + fileName);
			}
			else {
				throw new Exception("Cannot write to file " + fileName);
			}
		}
	}

	private Attribute createIdentColumn() {
		Attribute attr = new Attribute();
		attr.setName("ident");
		attr.setGeneric_type("number");
		attr.setSubtype("integer");
		attr.setNullable(false);
		attr.setSize(null);
		attr.setAutonum(true);
		return attr;
	}
}
