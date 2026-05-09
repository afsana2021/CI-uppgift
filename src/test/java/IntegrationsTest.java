
import io.restassured.response.Response;
        import org.junit.jupiter.api.Test;

        import java.util.List;

        import static io.restassured.RestAssured.given;
        import static org.hamcrest.Matchers.*;
        import static org.junit.jupiter.api.Assertions.*;

public class IntegrationsTest  {

    @Test
    void testGetProductsStatusCode() {

        Response response =
                given()
                        .when()
                        .get("https://fakestoreapi.com/products");

        assertEquals(
                200,
                response.getStatusCode(),
                "Expected 200 locally. GitHub Actions may return 403 due to bot protection."
        );
    }

    // Validate product count
    @Test
    void testProductCount() {

        List<?> products =
                given()
                        .when()
                        .get("https://fakestoreapi.com/products")
                        .then()
                        .extract()
                        .jsonPath()
                        .getList("$");

        assertTrue(products.size() > 0);
    }

    // Validate specific fields
    @Test
    void testProductFields() {

        given()
                .when()
                .get("https://fakestoreapi.com/products/1")
                .then()
                .statusCode(200)
                .body("title", notNullValue())
                .body("price", notNullValue())
                .body("category", notNullValue());
    }

    // Validate specific product ID
    @Test
    void testSpecificProductId() {

        given()
                .when()
                .get("https://fakestoreapi.com/products/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }
}