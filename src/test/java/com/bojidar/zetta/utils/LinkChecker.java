package com.bojidar.zetta.utils;

import com.bojidar.zetta.models.LinkResult;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkChecker {

    private static final int TIMEOUT_MS = 10_000;
    private static final String OK = "OK";
    private static final String DEAD_LINK = "Dead link";
    private static final String NO_TITLE = "N/A";
    private static final int MAX_ATTEMPTS = 2;

    private final RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", TIMEOUT_MS)
                    .setParam("http.socket.timeout", TIMEOUT_MS));

    public LinkResult check(String url) {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                Response response = RestAssured
                        .given()
                        .config(config)
                        .redirects().follow(true)
                        .when()
                        .get(url);

                int statusCode = response.getStatusCode();
                String pageTitle = extractPageTitle(response);

                String result = statusCode >= 200 && statusCode < 300
                        ? OK
                        : DEAD_LINK;

                return new LinkResult(url, statusCode, pageTitle, result);

            } catch (Exception e) {
                if (attempt == MAX_ATTEMPTS) {
                    return new LinkResult(url, 0, NO_TITLE, DEAD_LINK);
                }
            }
        }

        throw new IllegalStateException("Link check finished without a result.");
    }

    private String extractPageTitle(Response response) {
        try {
            String html = response.getBody().asString();

            Pattern pattern = Pattern.compile(
                    "<title>(.*?)</title>",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL
            );

            Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                return matcher.group(1).trim();
            }

            return NO_TITLE;

        } catch (Exception e) {
            return NO_TITLE;
        }
    }
}
