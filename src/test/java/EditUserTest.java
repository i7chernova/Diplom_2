import dto.User;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;

public class EditUserTest extends BaseTest {

    @Test
    @Description("Проверка успешного изменения email авторизованным пользователем")
    public void testUpdateEmailWithAuth() {
        testUser = generateRandomUser();

        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);
        User updatedUser = new User("newemail" + System.currentTimeMillis() + "@ya.ru", testUser.getPassword(), testUser.getName());

        Response updateResponse = userClient.updateUserWithAuth(updatedUser, accessToken);

        updateResponse.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("user.email", is(updatedUser.getEmail()));
    }

    @Test
    @Description("Проверка успешного изменения имени авторизованным пользователем")
    public void testUpdateNameWithAuth() {
        testUser = generateRandomUser();

        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);
        User updatedUser = new User(testUser.getEmail(), testUser.getPassword(), "NewName" + System.currentTimeMillis());

        Response updateResponse = userClient.updateUserWithAuth(updatedUser, accessToken);

        updateResponse.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("user.name", is(updatedUser.getName()));
    }

    @Test
    @Description("Проверка ошибки при изменении данных без авторизации")
    public void testUpdateWithoutAuth() {
        testUser = generateRandomUser();

        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);
        User updatedUser = new User("newemail@ya.ru", "newpass", "NewName");

        Response updateResponse = userClient.updateUserWithoutAuth(updatedUser);

        updateResponse.then().log().all().statusCode(401).and().assertThat().body("success", is(false))
                .and().body("message", is("You should be authorised"));
    }
}