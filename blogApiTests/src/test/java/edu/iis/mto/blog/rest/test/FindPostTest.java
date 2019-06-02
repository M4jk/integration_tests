package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.hasItems;

public class FindPostTest extends FunctionalTests {
    private static final String USER_API_POST = "/blog/user/{userId}/like/{postId}";
    private static final String USER_API = "/blog/user/{id}/post";

    @Test
    public void findBlogPostWhenOwnerOfThePostIsRemovedReturnsBadRequestStatusCode() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .get(USER_API, 4);
    }

    @Test
    public void findBlogPostShouldReturnCorrectAmountOfLikes() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("likesCount", hasItems(0))
                   .when()
                   .get(USER_API, 1);

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API_POST, 3, 1);

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("likesCount", hasItems(1))
                   .when()
                   .get(USER_API, 1);
    }

}
