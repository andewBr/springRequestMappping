import book.Account;
import book.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;


public class RestAssuredTest {
    private static Account account;

    private static int counter = 2;

    @Before
    public void postUser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String url = "http://localhost:8090/user/addUser";
        account = new Account("Bob", "password");
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON).body(mapper.writeValueAsBytes(account));
        Response response = requestSpecification.post(url);
        response.prettyPrint();
        response.then()
                .statusCode(SC_CREATED)
                .log();

    }

    @After
    public void removeUser() {
        String url = "http://localhost:8090/user/removeUser/Bob";
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON);
        Response response = requestSpecification.delete(url);
        response.prettyPrint();
        response.then()
                .statusCode(SC_OK)
                .log();
    }

    @Test
    public void postAndRemoveBook() throws JsonProcessingException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        String url = "http://localhost:8090/Bob/books";
        Book book = new Book(account, "Anna Karenina", "Leo Tolstoy", "description");
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON).body(mapper.writeValueAsBytes(book));
        Response response = requestSpecification.post(url);
        response.prettyPrint();
        response.then()
                .statusCode(SC_CREATED)
                .log();
        url = "http://localhost:8090/Bob/books/removeBook/" + (Integer.parseInt(getLastId()) + 1);
        counter = counter + 2;
        requestSpecification = given().contentType(ContentType.JSON);
        response = requestSpecification.delete(url);
        response.prettyPrint();
        response.then()
                .statusCode(SC_OK)
                .log();
    }

    private String getLastId() throws JSONException {
        String url = "http://localhost:8090/user/allUsers";
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON);
        Response response = requestSpecification.get(url);
        response.prettyPrint();
        JSONArray jsonResponse = new JSONArray(response.asString());
        return jsonResponse.getJSONObject(0).getString("id");
    }

}
