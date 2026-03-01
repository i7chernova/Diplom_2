import client.UserClient;
import dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

public class BaseTest {
    UserClient userClient;
    User testUser;
    String accessToken;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    User generateRandomUser() {
        Random random = new Random();
        String name = "user" + random.nextInt(1000);
        String email = name + "@ya.ru";
        String password = "password" + random.nextInt(1000);

        return new User(email, password, name);
    }

    User generateUserWithoutEmail() {
        return new User(null, "pass3579", "User");
    }

    User generateUserWithoutPassword() {
        return new User("user@ya.ru", null, "User");
    }

    User generateUserWithoutName() {
        return new User("user@ya.ru", "pass3579", null);
    }
}
