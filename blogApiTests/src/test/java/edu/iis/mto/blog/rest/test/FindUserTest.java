package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.is;

public class FindUserTest extends FunctionalTests {
    private static final String USER_API = "/blog/user/find";

    @Test
    public void findUserByEmailShouldReturnThreeUsers() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", "@domain")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(3))
                   .when()
                   .get(USER_API);
    }

    @Test
    public void findUserBySamePartInFirstNameShouldReturnTwoUsers() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", "jo")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(2))
                   .when()
                   .get(USER_API);
    }

    @Test
    public void findUserByLastNameShouldReturnOneUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", "Smith")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(1))
                   .when()
                   .get(USER_API);
    }

    @Test
    public void findUserShouldNotReturnRemovedUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", "ved")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(0))
                   .when()
                   .get(USER_API);
    }

}
