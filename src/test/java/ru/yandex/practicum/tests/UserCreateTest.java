package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.ApiErrors;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.steps.UserSteps;

import static org.hamcrest.Matchers.is;

public class UserCreateTest extends BaseTest {

    private User user;
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка, что можно создать пользователя передав все обязательные поля")
    public void createUserWithRequiredFieldsReturn200() {
        user = new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .withName(faker.name().name());
        ValidatableResponse userResponse = userSteps.createUser(user);
        accessToken = userResponse.extract()
                .body()
                .path("accessToken");
        userResponse
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание существующего пользователя")
    @Description("Проверка, что нельзя создать пользователя, который уже зарегистрирован")
    public void createExistedUserReturn403() {
        user = new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .withName(faker.name().name());
        ValidatableResponse userResponse = userSteps.createUser(user);
        accessToken = userResponse.extract()
                .body()
                .path("accessToken");
        userSteps.createUser(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_CREATE_ERROR_USER_EXISTS));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка, что нельзя создать пользователя без обязательного поля email")
    public void createUserWithoutEmailReturn403() {
        user = new User()
                .withPassword(faker.internet().password())
                .withName(faker.name().name());
        userSteps.createUser(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_CREATE_ERROR_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка, что нельзя создать пользователя без обязательного поля password")
    public void createUserWithoutPasswordReturn403() {
        user = new User()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().name());
        userSteps.createUser(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_CREATE_ERROR_REQUIRED_FIELDS));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка, что нельзя создать пользователя без обязательного поля name")
    public void createUserWithoutNameReturn403() {
        user = new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password());
        userSteps.createUser(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is(ApiErrors.USER_CREATE_ERROR_REQUIRED_FIELDS));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
