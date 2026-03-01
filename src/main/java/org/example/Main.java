package org.example;

import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.exception.ValidationException;
import org.example.service.CsvUserWriter;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        UserService service = new UserService();

        // =========================
        // Exceptions + try/catch/finally
        // =========================
        try {
            service.add(new UserDto(1, "Alice", 23, Status.ACTIVE));
            service.add(new UserDto(2, "Bob", 17, Status.NEW));
            service.add(new UserDto(3, "Charlie", 40, Status.DISABLED));
            service.add(new UserDto(4, "Daniel", 23, Status.ACTIVE));

            // Специально создаём ошибку (дубликат id)
            service.add(new UserDto(3, "Duplicate", 10, Status.NEW));

        } catch (ValidationException e) {
            // Базовая обработка: понятное сообщение
            System.out.println("Ошибка валидации: " + e.getMessage());

        } catch (RuntimeException e) {
            // Непредвиденная ошибка: сообщаем и пробрасываем
            System.out.println("Непредвиденная ошибка: " + e.getMessage());
            throw e;

        } finally {
            // finally выполняется всегда
            System.out.println("Инициализация завершена\n");
        }

        // =========================
        // try-with-resources: реальный ресурс (CSV)
        // =========================
        try (CsvUserWriter writer = new CsvUserWriter("users.csv")) {
            writer.writeAll(service.listAll());
            System.out.println("Пользователи записаны в users.csv");

        } catch (IOException e) {
            // Базовый error handling: понятное сообщение + контекст
            System.out.println("Ошибка записи CSV: " + e.getMessage());
        }

        System.out.println();

        // =========================
        // Lambda + Stream API
        // =========================

        // Лямбда через свой интерфейс UserFilter (без дженериков)
        List<UserDto> adultsActive = service.find(u ->
                u.getAge() >= 18 && u.getStatus() == Status.ACTIVE
        );

        System.out.println("Взрослые ACTIVE:");
        adultsActive.forEach(System.out::println); // method reference

        // Статистика по статусам
        Map<Status, Long> byStatus = service.countByStatus();
        System.out.println("\nКоличество по статусам:");
        byStatus.forEach((k, v) -> System.out.println(k + " = " + v)); // lambda

        System.out.println("\nСредний возраст: " + service.averageAge());

        // =========================
        // Error handling intro через OperationResult
        // =========================
        System.out.println("\nПоиск пользователя:");

        service.findById(4)
                .ifPresentOrElse(
                        u -> System.out.println("Найден: " + u),
                        () -> System.out.println("Пользователь не найден")
                );

        service.findById(2)
                .ifPresentOrElse(
                        u -> System.out.println("Найден: " + u),
                        () -> System.out.println("Пользователь не найден")
                );
    }
}