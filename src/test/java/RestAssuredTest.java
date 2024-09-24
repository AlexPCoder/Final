import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

public class RestAssuredTest {

    private RequestSpecification spec;
    private String authToken;

    @BeforeMethod
    public void authenticate() {
        // Create JSON body for the request
        JSONObject body = new JSONObject();
        body.put("username", "admin");
        body.put("password", "password123");

        // Initialize the RequestSpecification using RequestSpecBuilder
        spec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com") // Base URL for the API
                .setContentType("application/json")                 // Default content type
                .build();

        // Perform POST request to the auth endpoint
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")  // Add the Content-Type header
                .body(body.toString())  // Send JSON body as a string
                .post("https://restful-booker.herokuapp.com/auth");  // Auth endpoint

        // Validate the status code is 200
        response.then().statusCode(200);

        // Pretty print the response for debugging purposes
        response.prettyPrint();

        // Parse the response to extract the token
        JSONObject jsonResponse = new JSONObject(response.asString());
        authToken = jsonResponse.getString("token");

        // Print the extracted token
        System.out.println("Authentication Token: " + authToken);
    }

    @Test
    public void createBookingTest() {
        // Create request body for booking
        JSONObject body = new JSONObject();
        body.put("firstname", "Johny");
        body.put("lastname", "SilverHand");
        body.put("totalprice", 123);
        body.put("depositpaid", true);
        body.put("additionalneeds", "Breakfast");

        // Create the nested object for bookingdates
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2025-01-01");
        bookingDates.put("checkout", "2025-01-01");

        // Add the nested object to the main body
        body.put("bookingdates", bookingDates);

        // Send POST request to create a booking with headers
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")       // Content-Type header
                .header("Accept", "application/json")             // Accept header
                .header("Authorization", "Basic " + authToken)  // Basic Auth header
                .body(body.toString())                            // Request body
                .post("https://restful-booker.herokuapp.com/booking");

        // Extract the bookingid from the response
        int bookingId = response.jsonPath().getInt("bookingid");

        System.out.println("Booking ID: " + bookingId);

        // Perform a GET request to retrieve the booking details using the extracted bookingId
        Response getResponse = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .get("https://restful-booker.herokuapp.com/booking/" + bookingId);

        // Validate the response status code and body content for the created booking
        getResponse.then()
                .statusCode(200)
                .body("firstname", equalTo("Johny"))
                .body("lastname", equalTo("SilverHand"))
                .body("totalprice", equalTo(123))
                .body("depositpaid", equalTo(true))
                .body("bookingdates.checkin", equalTo("2025-01-01"))
                .body("bookingdates.checkout", equalTo("2025-01-01"))
                .body("additionalneeds", equalTo("Breakfast"));
    }

    @Test
    public void getBookingsTest() {
        Response response = RestAssured.given().log().all().spec(spec).get("https://restful-booker.herokuapp.com/booking/");
        response.then()
                .statusCode(200)
                .body("bookingid", everyItem(greaterThan(0)));
    }

    @Test
    public void deleteBookingTest() {
        int bookingId = 1;

        // Perform DELETE request to delete the booking
        Response deleteResponse = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .cookie("token", authToken)
                .delete("https://restful-booker.herokuapp.com/booking/" + bookingId);

        // Validate the response status code
        deleteResponse.then()
                .statusCode(201)
                .log().all();
    }
}
