package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.ApiErrors;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.UserSteps;

import java.util.List;

import static org.hamcrest.Matchers.is;


public class OrderCreateTest extends BaseTest {

    private Order order;
    private OrderSteps orderSteps;
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        createUser();
        initOrder();
    }

    private void createUser() {
        userSteps = new UserSteps();
        User user = new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .withName(faker.name().name());
        accessToken = userSteps.createUser(user).extract()
                .body()
                .path("accessToken");
    }

    private void initOrder() {
        orderSteps = new OrderSteps();
        List<String> ingredientsId = orderSteps.getIngredientsId();
        int maxIngredientsIndex = ingredientsId.size() < 3 ? 1 : ingredientsId.size() / 2;
        List<String> ingredientsForOrder = ingredientsId.subList(
                0, faker.number().numberBetween(1, maxIngredientsIndex)
        );
        order = new Order().withIngredients(ingredientsForOrder);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    @Description("Проверка, что можно создать заказ с ингредиентами авторизованным пользователем")
    public void createOrderWithIngredientsAuthorizedUserReturn200() {
        orderSteps.createOrder(order, accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизованным пользователем")
    @Description("Проверка, что можно создать заказ с ингредиентами неавторизованным пользователем")
    public void createOrderWithIngredientsUnauthorizedUserReturn200() {
        orderSteps.createOrder(order)
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка, что нельзя создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsReturn400() {
        order = new Order().withIngredients(List.of());
        orderSteps.createOrder(order, accessToken)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is(ApiErrors.ORDER_CREATE_ERROR_INGREDIENTS));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка, что нельзя создать заказ с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHashReturn500() {
        order = new Order().withIngredients(List.of("1234567890qwerty100", "1234567890qwerty101"));
        orderSteps.createOrder(order, accessToken)
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без поля ингредиенты")
    @Description("Проверка, что нельзя создать заказ без поля ингредиенты")
    public void createOrderWithoutFieldIngredientsReturn400() {
        order = new Order().withIngredients(null);
        orderSteps.createOrder(order, accessToken)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is(ApiErrors.ORDER_CREATE_ERROR_INGREDIENTS));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
