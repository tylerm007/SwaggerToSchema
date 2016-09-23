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
		//... does not work ....generateSchema("http://liveapi.dreamfactory.com/df-swagger-ui/dist/index.html",false);
		generateSchema("http://petstore.swagger.io/v2/swagger.json", true, 9);
		generateSchema("https://dev.expressologic.com/rest/default/demo_mysql/v1/@docs", false, 47);
		generateSchema("https://dev.expressologic.com/rest/default/demo/v1/@docs", false, 26);
		generateSchema("uber.json", true, 7);
		generateSchema("http://54.171.250.144/api-docs/customerManagementSwagger.json ", true, 45);
		//generateSchema("//Users/banty01/swagger.json",true);
		generateSchema("http://devdocs.magento.com/swagger/schemas/latest-2.1.schema.json", true, 265);

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
