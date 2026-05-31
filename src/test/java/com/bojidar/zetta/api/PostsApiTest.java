package com.bojidar.zetta.api;

import com.bojidar.zetta.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class PostsApiTest {

    @BeforeClass
    public void setupApi() {
        RestAssured.baseURI = Config.jsonPlaceholderUrl();
    }

    @DataProvider(name = "validUserIds")
    public Object[][] validUserIds() {
        return new Object[][]{
                {1},
                {5},
                {10}
        };
    }

    @Test(dataProvider = "validUserIds", description = "GET /posts should return exactly 10 posts for valid users")
    public void shouldReturnTenPostsForValidUser(int userId) {
        List<Integer> postIds = RestAssured
                .given()
                .queryParam("userId", userId)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id");

        Assert.assertEquals(postIds.size(), 10,
                "User " + userId + " should have exactly 10 posts.");
    }

    @Test(description = "GET /posts should return empty list for unknown user")
    public void shouldReturnEmptyListForUnknownUser() {
        Response response = RestAssured
                .given()
                .queryParam("userId", 999)
                .when()
                .get("/posts");

        Assert.assertEquals(response.getStatusCode(), 200,
                "Unknown user request should return HTTP 200.");

        List<Object> posts = response.jsonPath().getList("");

        Assert.assertTrue(posts.isEmpty(),
                "Unknown user should return an empty posts array.");
    }

    @Test(description = "GET /posts should return unique post ids")
    public void shouldReturnUniquePostIds() {
        List<Integer> postIds = RestAssured
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id");

        long uniqueIdsCount = postIds.stream().distinct().count();

        Assert.assertEquals(uniqueIdsCount, (long) postIds.size(),
                "Every post id should be unique.");
    }

    @Test(description = "GET /posts/1 should return expected post fields and types")
    public void shouldReturnExpectedPostSchema() {
        Response response = RestAssured
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Object id = response.path("id");
        Object userId = response.path("userId");
        Object title = response.path("title");
        Object body = response.path("body");

        Assert.assertNotNull(id, "Post id should not be null.");
        Assert.assertNotNull(userId, "Post userId should not be null.");
        Assert.assertNotNull(title, "Post title should not be null.");
        Assert.assertNotNull(body, "Post body should not be null.");

        Assert.assertTrue(id instanceof Integer, "Post id should be an integer.");
        Assert.assertTrue(userId instanceof Integer, "Post userId should be an integer.");
        Assert.assertTrue(title instanceof String, "Post title should be a string.");
        Assert.assertTrue(body instanceof String, "Post body should be a string.");

        Assert.assertFalse(((String) title).isBlank(),
                "Post title should not be empty.");

        Assert.assertFalse(((String) body).isBlank(),
                "Post body should not be empty.");
    }

    @Test(description = "POST /posts should create a post and echo request body")
    public void shouldCreatePost() {
        Map<String, Object> requestBody = Map.of(
                "title", "Zetta QA Task Title",
                "body", "This is the automation test body.",
                "userId", 999
        );

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Assert.assertEquals(response.path("title"), requestBody.get("title"),
                "Created post title should match request body.");

        Assert.assertEquals(response.path("body"), requestBody.get("body"),
                "Created post body should match request body.");

        Assert.assertEquals(response.path("userId"), requestBody.get("userId"),
                "Created post userId should match request body.");

        Assert.assertNotNull(response.path("id"),
                "Created post response should contain generated id.");
    }
}