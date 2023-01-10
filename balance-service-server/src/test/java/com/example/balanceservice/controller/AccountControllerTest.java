package com.example.balanceservice.controller;

import com.example.balanceservice.entity.AccountEntity;
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

    private final static String BASE_PATH_GET_BALANCE = "http://localhost:8091/v1/account/getBalance/";

    private final static String BASE_PATH_CHANGE_BALANCE = "http://localhost:8091/v1/account/changeBalance/";

    private final static int UNKNOWN_ID = 4;

    private final static AccountEntity account = new AccountEntity(1L, 1L);

    @Test
    @DisplayName("Тестирование контроллера запроса баланса, должен быть статус 200")
    void getBalanceById_whenGetBalance_thenStatus200() {
        RestAssured.
                when()
                .get(BASE_PATH_GET_BALANCE + account.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
    }

    @Test
    @DisplayName("Тестирование контроллера запроса баланса, не существующий пользователь, должен быть статус 400")
    void getBalanceById_whenGetBalance_thenStatus400() {
        String responseString =
                RestAssured.
                        when()
                        .get(BASE_PATH_GET_BALANCE + UNKNOWN_ID)
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
        requestBody.put("id", account.getId());
        requestBody.put("amount", 1);
        RequestSpecification request = RestAssured.given().log().all();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post(BASE_PATH_CHANGE_BALANCE);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Тестирование контроллера изменение баланса, не существующий пользователь, должен быть статус 400")
    void changeBalanceById_whenChangeBalance_thenStatus400() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", UNKNOWN_ID);
        requestBody.put("amount", 1);
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestBody.toJSONString())
                .when()
                .post(BASE_PATH_CHANGE_BALANCE)
                .then().log().all()
                .assertThat()
                .statusCode(400).toString();
    }

    @Test
    @DisplayName("Тестирование контроллера изменение баланса, не корректная сумма, должен быть статус 400")
    void changeBalanceById_whenChangeBalance_thenStatus400_2() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", account.getId());
        requestBody.put("amount", -20000);
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestBody.toJSONString())
                .when()
                .post(BASE_PATH_CHANGE_BALANCE)
                .then().log().all()
                .assertThat()
                .statusCode(400);
    }
}