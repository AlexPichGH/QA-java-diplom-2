package ru.yandex.practicum;

public class ApiErrors {

    public static final String USER_CREATE_ERROR_USER_EXISTS = "User already exists";
    public static final String USER_CREATE_ERROR_REQUIRED_FIELDS = "Email, password and name are required fields";

    public static final String USER_LOGIN_ERROR_INCORRECT_FIELDS = "email or password are incorrect";

    public static final String ORDER_CREATE_ERROR_INGREDIENTS = "Ingredient ids must be provided";
}
