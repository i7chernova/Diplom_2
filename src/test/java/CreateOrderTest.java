import client.IngredientsClient;
import client.OrderClient;
import dto.Order;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseTest {

    private OrderClient orderClient;
    private String ingrId1;
    private String ingrId2;

    @BeforeEach
    public void setUpOrderTest() {
        orderClient = new OrderClient();
        IngredientsClient ingredientsClient = new IngredientsClient();

        Response ingredientsResponse = ingredientsClient.getIngredients();
        ingrId1 = ingredientsClient.getFirstIngredientId(ingredientsResponse);
        ingrId2 = ingredientsClient.getSecondIngredientId(ingredientsResponse);
    }

    @Test
    @Description("Проверка успешного создания заказа авторизованным пользователем с ингредиентами")
    public void testCreateOrderWithAuthAndIngredients() {
        testUser = generateRandomUser();
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        List<String> ingredients = Arrays.asList(ingrId1, ingrId2);
        Order order = new Order(ingredients);

        Response orderResponse = orderClient.createOrderWithAuth(order, accessToken);

        orderResponse.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("name", notNullValue())
                .and().body("order.number", notNullValue());
    }

    @Test
    @Description("Проверка создания заказа неавторизованным пользователем")
    public void testCreateOrderWithoutAuthWithIngredients() {
        List<String> ingredients = Arrays.asList(ingrId1, ingrId2);
        Order order = new Order(ingredients);

        Response orderResponse = orderClient.createOrderWithoutAuth(order);

        orderResponse.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("name", notNullValue())
                .and().body("order.number", notNullValue());
    }

    @Test
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        testUser = generateRandomUser();
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        Order order = new Order(Collections.emptyList());

        Response orderResponse = orderClient.createOrderWithAuth(order, accessToken);

        orderResponse.then().log().all().statusCode(400).and().assertThat().body("success", is(false))
                .and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @Description("Проверка ошибки при создании заказа с невалидным ID ингредиента")
    public void testCreateOrderWithInvalidIngredientHash() {
        testUser = generateRandomUser();
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        List<String> ingredients = Arrays.asList("385urhwuwhgdvfhs1123", ingrId2);
        Order order = new Order(ingredients);

        Response orderResponse = orderClient.createOrderWithAuth(order, accessToken);
        orderResponse.then().log().all().statusCode(500);
    }
}