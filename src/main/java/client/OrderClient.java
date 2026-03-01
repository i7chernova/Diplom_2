package client;

import configuration.ApiConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import dto.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS = "/orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String token) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .header("Authorization", token)
                .body(order)
                .when().post(ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(order)
                .when().post(ORDERS);
    }

    @Step("Получение заказов пользователя с авторизацией")
    public Response getUserOrdersWithAuth(String token) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .header("Authorization", token)
                .when().get(ORDERS);
    }

    @Step("Получение заказов пользователя без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when().get(ORDERS);
    }
}