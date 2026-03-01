package org.example.exception;

/**
 * Исключение уровня валидации/бизнес-правил.
 * Это "нормальная" ошибка, которую можно показать пользователю понятным сообщением.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}