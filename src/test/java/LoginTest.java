import dto.Credentials;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest extends BaseTest {

    @Test
    @Description("Проверка успешной авторизации существующего пользователя")
    public void testLoginExistingUser() {
        testUser = generateRandomUser();

        // Создаем пользователя
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        // Авторизуемся
        Credentials credentials = new Credentials(testUser.getEmail(), testUser.getPassword());
        Response loginResponse = userClient.loginUser(credentials);

        loginResponse.then().log().all().statusCode(200).and().assertThat().body("success", is(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", is(testUser.getEmail()))
                .and().body("user.name", is(testUser.getName()));
    }

    @Test
    @Description("Проверка ошибки при авторизации с неверным email")
    public void testLoginWithWrongEmail() {
        testUser = generateRandomUser();

        // Создаем пользователя
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        // Пытаемся залогиниться с неверным email
        Credentials wrongCredentials = new Credentials("wrong@yaaaa.ru", testUser.getPassword());
        Response loginResponse = userClient.loginUser(wrongCredentials);

        loginResponse.then().log().all().statusCode(401).and().assertThat().body("success", is(false))
                .and().body("message", is("email or password are incorrect"));
    }

    @Test
    @Description("Проверка ошибки при авторизации с неверным паролем")
    public void testLoginWithWrongPassword() {
        testUser = generateRandomUser();

        // Создаем пользователя
        Response createResponse = userClient.createUser(testUser);
        accessToken = userClient.extractToken(createResponse);

        // Пытаемся залогиниться с неверным паролем
        Credentials wrongCredentials = new Credentials(testUser.getEmail(), "wrongpassword");
        Response loginResponse = userClient.loginUser(wrongCredentials);

        loginResponse.then().log().all().statusCode(401).and().assertThat().body("success", is(false))
                        .and().body("message", is("email or password are incorrect"));
    }
}
