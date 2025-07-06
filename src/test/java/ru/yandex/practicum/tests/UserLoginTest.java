package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.ApiErrors;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.steps.UserSteps;

import static org.hamcrest.Matchers.is;

public class UserLoginTest extends BaseTest {

    private User user;
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        user = new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .withName(faker.name().name());
        userSteps = new UserSteps();
        accessToken = userSteps.createUser(user).extract()
                .body()
                .path("accessToken");
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверка, что можно авторизоваться с данными существующего пользователя")
    public void loginUserWithExistedUserReturn200() {
        userSteps.loginUser(user)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверка, что нельзя авторизоваться с несуществующим логином")
    public void loginUserWithNonExistentEmailReturn401() {
        User userWithWrongEmail = user.withEmail(faker.internet().emailAddress());
        userSteps.loginUser(userWithWrongEmail)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_LOGIN_ERROR_INCORRECT_FIELDS));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверка, что нельзя авторизоваться с несуществующим паролем")
    public void loginUserWithNonExistentPasswordReturn401() {
        User userWithWrongPassword = user.withPassword(faker.internet().password());
        userSteps.loginUser(userWithWrongPassword)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_LOGIN_ERROR_INCORRECT_FIELDS));
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    @Description("Проверка, что нельзя авторизоваться без обязательного поля email")
    public void loginUserWithoutEmailReturn401() {
        User userWithoutEmail = user.withEmail(null);
        userSteps.loginUser(userWithoutEmail)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_LOGIN_ERROR_INCORRECT_FIELDS));
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    @Description("Проверка, что нельзя авторизоваться без обязательного поля password")
    public void loginUserWithoutPasswordReturn401() {
        User userWithoutPassword = user.withPassword(null);
        userSteps.loginUser(userWithoutPassword)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_LOGIN_ERROR_INCORRECT_FIELDS));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
