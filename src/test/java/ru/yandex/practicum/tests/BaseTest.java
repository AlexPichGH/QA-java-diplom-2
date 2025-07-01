package ru.yandex.practicum.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.Before;

import static ru.yandex.practicum.ApiConfig.BASE_URL;

public class BaseTest {
    protected Faker faker;

    @Before
    public void init() {
        // инициализация datafaker для генерации тестовых данных
        faker = new Faker();
        // добавление логирования запросов и ответов
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        // настройка таймаутов
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 5000)
                        .setParam("http.socket.timeout", 5000)
                        .setParam("http.connection-manager.timeout", 5000));
        // настройка базового URL и ContentType
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}
