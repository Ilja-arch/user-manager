package org.example.service;

import org.example.dto.UserDto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Ресурс, который пишет пользователей в CSV.
 * Демонстрация try-with-resources + обработка ошибок.
 */
public class CsvUserWriter implements AutoCloseable {

    private final PrintWriter writer;
    private boolean closed;

    public CsvUserWriter(String fileName) throws IOException {
        this.writer = new PrintWriter(new FileWriter(fileName));
        this.closed = false;

        // Заголовок CSV
        writer.println("id,name,age,status");
    }

    public void write(UserDto user) {
        if (closed) {
            throw new IllegalStateException("Файл уже закрыт");
        }
        writer.println(
                user.getId() + "," +
                escapeCsv(user.getName()) + "," +
                user.getAge() + "," +
                user.getStatus()
        );
    }

    public void writeAll(List<UserDto> users) {
        // Stream API + method reference
        users.stream().forEach(this::write);
    }

    @Override
    public void close() {
        closed = true;
        writer.flush();
        writer.close();
        System.out.println("CSV файл закрыт");
    }

    private static String escapeCsv(String value) {
        // Минимальная обработка: если есть запятая/кавычка/перенос строки — экранируем кавычками
        if (value == null) return "";
        boolean needQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String v = value.replace("\"", "\"\"");
        return needQuotes ? "\"" + v + "\"" : v;
    }
}