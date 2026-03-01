import client.IngredientsClient;
import client.OrderClient;
import dto.Order;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderUserTest extends BaseTest {

    private OrderClient orderClient;
    private IngredientsClient ingredientsClient;
    private String validIngredientId;
    private String anotherValidIngredientId;

    @BeforeEach
    public void setUpOrdersTest() {
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();

        Response ingredientsResponse = ingredientsClient.getIngredients();
        validIngredientId = ingredientsClient.getFirstIngredientId(ingredientsResponse);
        anotherValidIngredientId = ingredientsClient.getSecondIngredientId(ingredientsResponse);
    }

    @Test
    @Description("Проверка успешного получения списка заказов авторизованным пользователем")
    public void testGetUserOrdersWithAuth() {
        testUser = generateRandomUser();
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        // Создаем заказ для пользователя
        List<String> ingredients = Arrays.asList(validIngredientId, anotherValidIngredientId);
        Order order = new Order(ingredients);
        orderClient.createOrderWithAuth(order, accessToken);

        // Получаем заказы пользователя
        Response ordersResponse = orderClient.getUserOrdersWithAuth(accessToken);

        ordersResponse.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("orders", notNullValue())
                .and().body("orders.number", notNullValue());
    }

    @Test
    @Description("Проверка ошибки при получении заказов без авторизации")
    public void testGetUserOrdersWithoutAuth() {
        Response ordersResponse = orderClient.getUserOrdersWithoutAuth();

        ordersResponse.then().log().all().statusCode(401).and().assertThat().body("success", is(false))
                .and().body("message", is("You should be authorised"));
    }
}