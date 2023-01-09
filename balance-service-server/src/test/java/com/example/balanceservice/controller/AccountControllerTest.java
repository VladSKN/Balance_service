package com.example.balanceservice.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * Тестирование функционала {@link com.example.balanceservice.controller.AccountController}.
 */
@Disabled("Тесты только на запущеном приложении")
@ActiveProfiles("test")
class AccountControllerTest {

    private final static String BASE_PATH = "http://localhost:8091/v1/account";

    @Test
    @DisplayName("Тестирование контроллера запроса баланса, должен быть статус 200")
    void getBalanceById_whenGetBalance_thenStatus200() {
        String responseString =
                RestAssured.
                        when()
                        .get(BASE_PATH + "/getBalance/" + 1)
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .asString();
        assertThat(responseString).isEqualTo("16771");
    }

    @Test
    @DisplayName("Тестирование контроллера запроса баланса, не существующий пользователь, должен быть статус 400")
    void getBalanceById_whenGetBalance_thenStatus400() {
        String responseString =
                RestAssured.
                        when()
                        .get(BASE_PATH + "/getBalance/4")
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(400)
                        .extract()
                        .asString();
        assertThat(responseString).isEqualTo("id not found");
    }

    @Test
    @DisplayName("Тестирование контроллера изменение баланса, должен быть статус 200")
    void changeBalanceById_whenChangeBalance_thenStatus200() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 2);
        requestBody.put("amount", 1);
        RequestSpecification request = RestAssured.given().log().all();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post(BASE_PATH + "/changeBalance/");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Тестирование контроллера изменение баланса, не существующий пользователь, должен быть статус 400")
    void changeBalanceById_whenChangeBalance_thenStatus400() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 4);
        requestBody.put("amount", 1);
        RequestSpecification request = RestAssured.given().log().all();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post(BASE_PATH + "/changeBalance/");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Тестирование контроллера изменение баланса, не корректная сумма, должен быть статус 400")
    void changeBalanceById_whenChangeBalance_thenStatus400_2() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 2);
        requestBody.put("amount", -2000);
        RequestSpecification request = RestAssured.given().log().all();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post(BASE_PATH + "/changeBalance/");
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }
}