import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


public class InvoiceNinjaTest {

    Response response;
    RestAssured restAssured;
    private static final String CONTENT_PATH="http://79.137.68.21/api/v1";
    private static final String HEADER_CONTENT_TYPE="application/json";
    private static final String HEADER_TOKEN="apoqghyipkpka2kya3drigita4fyucz3";
    private StringBuilder sBuilderContentPath= new StringBuilder(CONTENT_PATH);

    @BeforeMethod
    public void setUp() {
        restAssured = new RestAssured();

    }

    @Test
    public void nameOfClientsCityTest() {

                restAssured.given()
                .header("Content-Type", HEADER_CONTENT_TYPE)
                .header("X-Ninja-Token", HEADER_TOKEN)
                .when()
                .get(sBuilderContentPath.append("/clients/104").toString())
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType("application/json")
                .body("data.city", equalTo("Warszawa"));
    }

    @Test
    public void numberOfObjectsTest() {

        String nameOfSubPage = "invoices";
        int numberOfObjects = 15;

        restAssured
                .given()
                .header("Content-Type", HEADER_CONTENT_TYPE)
                .header("X-Ninja-Token", HEADER_TOKEN)
                .pathParam("subPage", nameOfSubPage)
                .when()
                .get(sBuilderContentPath.append("/{subPage}").toString())
                .then()
                .assertThat()
                .body("data", hasSize(numberOfObjects));
    }


    @DataProvider(name = "clientsAndNumberOfInvoices")
    public Object[][] createTestDataRecords() {
        return new Object[][]{
                {"Test-09WR", 105},
                {"Test-08WR", 104},
                {"Test-02WR", 98},
                {"Test-04WR", 100}
        };
    }

    @Test(dataProvider = "clientsAndNumberOfInvoices")
    public void numberOfInvoicesOfDifferentClientTest(String clientName, int clientId) {
        restAssured
                .given()
                .header("Content-Type", HEADER_CONTENT_TYPE)
                .header("X-Ninja-Token", HEADER_TOKEN)
                .pathParam("id", clientId).
                when().
                get(sBuilderContentPath.append("/clients/{id}").toString()).
                then().
                assertThat().
                body("data.name", equalTo(clientName));
    }


    @DataProvider(name = "inputData")
    public static Object[][] inputData() {
        return new Object[][]{
                {"Client-22WR", "Suwałki"},
                {"Client-23WR", "Gdańsk"}
        };
    }

    @Test(dataProvider = "inputData")
    public void putNewJsonTest(String clientName, String city) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", clientName);
        jsonObject.addProperty("city",city);

        response=restAssured
                .given()
                    .header("Content-Type", HEADER_CONTENT_TYPE)
                    .header("X-Ninja-Token", HEADER_TOKEN)
                    .body(jsonObject.toString())
                .when()
                    .post(sBuilderContentPath.append("/clients").toString())
                .then()
                    .assertThat()
                        .statusCode(200)
                    .and()
                        .body("data.name", equalTo(clientName))
                .extract()
                .response();
        response.print();
    }

    @Test
    public void deleteClientTest(){
        String clientId = "99";
        response=restAssured
                .given()
                .header("Content-Type", HEADER_CONTENT_TYPE)
                .header("X-Ninja-Token", HEADER_TOKEN)
                .delete(sBuilderContentPath.append("/clients/").append(clientId).toString())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();
        response.print();
    }

}

