import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


public class InvoiceNinjaTest {


    @Test
    public void nameOfClientsCityTest() {
        new RestAssured()
                .given()
                .header("Content-Type", "application/json")
                .header("X-Ninja-Token", "apoqghyipkpka2kya3drigita4fyucz3")
                .when()
                .get("http://79.137.68.21/api/v1/clients/104")
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

        new RestAssured()
                .given()
                .header("Content-Type", "application/json")
                .header("X-Ninja-Token", "apoqghyipkpka2kya3drigita4fyucz3")
                .pathParam("subPage", nameOfSubPage)
                .when()
                .get("http://79.137.68.21/api/v1/{subPage}")
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
        new RestAssured()
                .given()
                .header("Content-Type", "application/json")
                .header("X-Ninja-Token", "apoqghyipkpka2kya3drigita4fyucz3")
                .pathParam("id", clientId).
                when().
                get("http://79.137.68.21/api/v1/clients/{id}").
                then().
                assertThat().
                body("data.name", equalTo(clientName));
    }


    @DataProvider(name = "inputData")
    public static Object[][] inputData() {
        return new Object[][]{
                {"Client-20WR", "Suwałki"},
                {"Client-21WR", "Gdańsk"}
        };
    }

    @Test(dataProvider = "inputData")
    public void putNewJsonTest(String clientName, String city) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", clientName);
        jsonObject.addProperty("city",city);

        new RestAssured()
                .given()
                    .header("Content-Type", "application/json")
                    .header("X-Ninja-Token", "apoqghyipkpka2kya3drigita4fyucz3")
                    .body(jsonObject.toString())
                .when()
                    .post("http://79.137.68.21/api/v1/clients")
                .then()
                    .assertThat()
                        .statusCode(200)
                    .and()
                        .body("data.name", equalTo(clientName));
    }
}

