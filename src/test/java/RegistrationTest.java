import dto.User;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationTest extends BaseTest {

    @Test
    @Description("Проверка успешного создания нового пользователя")
    public void testCreateUniqueUser() {
        testUser = generateRandomUser();

        Response response = userClient.createUser(testUser);
        accessToken = userClient.extractToken(response);

        response.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", is(testUser.getEmail()))
                .and().body("user.name", is(testUser.getName()));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя, который уже зарегистрирован")
    public void testCreateExistingUser() {
        testUser = generateRandomUser();

        // Создаем пользователя первый раз
        Response firstResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(firstResponse);

        // Пытаемся создать того же пользователя повторно
        Response secondResponse = userClient.createUser(testUser);

        secondResponse.then().log().all().statusCode(403).and().assertThat().body("success", is(false))
                .and().body("message", is("User already exists"));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без email")
    public void testCreateUserWithoutEmail() {
        User userWithoutEmail = generateUserWithoutEmail();
        Response response = userClient.createUser(userWithoutEmail);

        response.then().log().all().statusCode(403).and().assertThat().body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без пароля")
    public void testCreateUserWithoutPassword() {
        User userWithoutPassword = generateUserWithoutPassword();
        Response response = userClient.createUser(userWithoutPassword);

        response.then().log().all().statusCode(403).and().assertThat().body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без имени")
    public void testCreateUserWithoutName() {
        User userWithoutName = generateUserWithoutName();
        Response response = userClient.createUser(userWithoutName);

        response.then().log().all().statusCode(403).and().assertThat().body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }
}