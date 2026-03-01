package client;

import configuration.ApiConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient {
    private static final String INGREDIENTS = "/ingredients";

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when().get(INGREDIENTS);
    }

    @Step("Извлечение ID первого ингредиента")
    public String getFirstIngredientId(Response response) {
        return response.jsonPath().getString("data[0]._id");
    }

    @Step("Извлечение ID второго ингредиента")
    public String getSecondIngredientId(Response response) {
        return response.jsonPath().getString("data[1]._id");
    }
}