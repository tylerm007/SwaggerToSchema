import com.ca.swagger.schema.objects.SwaggerToSchema;

public class SwaggerConvertTest {
	
	private static SwaggerToSchema s2schema;
	
	public static void main(String[] args) {
		s2schema = new SwaggerToSchema();
		test();
	}
	private static void test() {
		//... does not work ....generateSchema("http://liveapi.dreamfactory.com/df-swagger-ui/dist/index.html",false);
		generateSchema("http://petstore.swagger.io/v2/swagger.json", true);
		generateSchema("https://dev.expressologic.com/rest/default/demo_mysql/v1/@docs",false);
		generateSchema("https://dev.expressologic.com/rest/default/demo/v1/@docs",false);
		generateSchema("uber.json", true);
		generateSchema("http://54.171.250.144/api-docs/customerManagementSwagger.json ",true);
		//generateSchema("//Users/banty01/swagger.json",true);
		generateSchema("http://devdocs.magento.com/swagger/schemas/latest-2.1.schema.json", true);
		
	}
	
	public static String generateSchema(String swaggerDoc, boolean test) {
		String content = s2schema.generateSchema(swaggerDoc, test);
		return content;
	}

}
