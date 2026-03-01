package client;

import configuration.ApiConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import dto.Credentials;
import dto.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String AUTH_REGISTER = "/auth/register";
    private static final String AUTH_LOGIN = "/auth/login";
    private static final String AUTH_USER = "/auth/user";

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(user)
                .when().post(AUTH_REGISTER);
    }

    @Step("Логин пользователя")
    public Response loginUser(Credentials credentials) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(credentials)
                .when().post(AUTH_LOGIN);
    }

    @Step("Обновление данных пользователя с авторизацией")
    public Response updateUserWithAuth(User user, String token) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .header("Authorization", token)
                .body(user)
                .when().patch(AUTH_USER);
    }

    @Step("Обновление данных пользователя без авторизации")
    public Response updateUserWithoutAuth(User user) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(user)
                .when().patch(AUTH_USER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .header("Authorization", token)
                .when().delete(AUTH_USER);
    }

    @Step("Извлечение токена из ответа")
    public String extractToken(Response response) {
        return response.jsonPath().getString("accessToken");
    }
}
