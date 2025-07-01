package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.ApiConfig;
import ru.yandex.practicum.models.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .when()
                .post(ApiConfig.USER_CREATE_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .body(user)
                .when()
                .post(ApiConfig.USER_LOGIN_ENDPOINT)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(ApiConfig.USER_DELETE_ENDPOINT)
                .then();
    }
}
