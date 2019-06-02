package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {
    private static final String USER_API = "/blog/user/{userId}/like/{postId}";

    @Test
    public void likeBlogPostWhenUserIsConfirmedReturnsOKStatusCode() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);
    }

    @Test
    public void likeBlogPostWhenUserIsNotConfirmedReturnsNotAcceptableStatusCode() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_NOT_ACCEPTABLE)
                   .when()
                   .post(USER_API, 2, 1);
    }

    @Test
    public void likeBlogPostWhenUserIsConfirmedAndOwnerOfThePostReturnsBadRequestStatusCode() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post(USER_API, 1, 1);
    }

    @Test
    public void likeBlogPostWhenUserIsConfirmedAndPostAlreadyLikedByHimReturnsOKStatus() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);
    }

}
