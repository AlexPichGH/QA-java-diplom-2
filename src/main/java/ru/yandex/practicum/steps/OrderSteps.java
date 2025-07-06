package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.ApiConfig;
import ru.yandex.practicum.models.Order;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ApiConfig.ORDER_CREATE_ENDPOINT)
                .then();
    }

    @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ApiConfig.ORDER_CREATE_ENDPOINT)
                .then();
    }

    @Step("Получение ингредиентов")
    public List<String> getIngredientsId() {
        return given()
                .get(ApiConfig.ORDER_GET_INGREDIENTS_ENDPOINT)
                .jsonPath()
                .getList("data._id");
    }
}
