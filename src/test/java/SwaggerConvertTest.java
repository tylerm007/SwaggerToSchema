import java.io.IOException;

import com.ca.swagger.schema.objects.SwaggerToSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SwaggerConvertTest {

	private static SwaggerToSchema s2schema;

	public static void main(String[] args) {
		s2schema = new SwaggerToSchema();
		try {
			test();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void test() throws Exception {
		String content;
		//... does not work ....generateSchema("http://liveapi.dreamfactory.com/df-swagger-ui/dist/index.html",false);
		// content = generateSchema("http://petstore.swagger.io/v2/swagger.json", true, 8);
		// content = generateSchema("https://dev.expressologic.com/rest/default/demo_mysql/v1/@docs", false, 45);
		//generateSchema("https://dev.expressologic.com/rest/default/demo/v1/@docs", false, 26);
		// content = generateSchema("demoSwagger.json", true, 49);
		// content = generateSchema("http://54.171.250.144/api-docs/customerManagementSwagger.json ", true, 28);
		//content = generateSchema("http://localhost:8080/APIServer/rest/default/demo_mysql/v1/@docs", true, 59);
		content = generateSchema("http://localhost:8080/APIServer/rest/default/demo/v1/@docs", true, 31);
		//generateSchema("//Users/banty01/swagger.json",true);
		//generateSchema("http://devdocs.magento.com/swagger/schemas/latest-2.1.schema.json", true, 172);
		s2schema.writeToFile("/Users/banty01/Downloads/schema.json", content);

	}

	public static String generateSchema(String swaggerDoc, boolean test, int expectedEntityCount) throws Exception {
		String content = s2schema.generateSchema(swaggerDoc, test);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(content);
		int count = countEntities(json);
		if (expectedEntityCount == count) {
			System.out.println("swagger endpoint "+swaggerDoc+" count = " + count);
		}
		else {
			throw new Exception("Entity count is " + count + " Excpected count was " + expectedEntityCount);
		}
		return content;
	}

	private static int countEntities(JsonNode json) {
		int count = 0;
		if (json != null && json.size() > 0) {
			for (int idx = 0; idx < json.size(); idx++) {
				JsonNode node = json.get(idx);
				count++;
			}
		}
		return count;
	}

}
