package org.example.service;

import org.example.dto.UserDto;
import org.example.exception.ValidationException;
import org.example.dto.Status;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис с минимальной логикой:
 * - хранение пользователей
 * - валидация
 * - несколько операций на Stream API
 * - базовый error handling через OperationResult
 */
public class UserService {

    private final List<UserDto> users = new ArrayList<>();

    public void add(UserDto user) {
        // Базовая валидация + исключения
        if (user == null) {
            throw new ValidationException("Пользователь не может быть null");
        }
        if (user.getId() <= 0) {
            throw new ValidationException("id должен быть положительным: " + user.getId());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (user.getAge() < 0) {
            throw new ValidationException("Возраст не может быть отрицательным: " + user.getAge());
        }
        if (user.getStatus() == null) {
            throw new ValidationException("Статус не может быть null");
        }

        // Stream + lambda: проверка на дубликаты по id
        boolean exists = users.stream().anyMatch(u -> u.getId() == user.getId());
        if (exists) {
            throw new ValidationException("Пользователь с таким id уже существует: " + user.getId());
        }

        users.add(user);
    }

    public List<UserDto> listAll() {
        return Collections.unmodifiableList(users);
    }

    public List<UserDto> find(UserFilter filter) {
        // Stream API + лямбда (filter передаётся извне)
        List<UserDto> listOfUsers = new ArrayList<>() ;
        for (UserDto user : users) {
            if (filter.test(user)) {
                listOfUsers.add(user);
            }
        }
        return listOfUsers;

    }

    public Map<Status, Long> countByStatus() {
        // Stream API: группировка + подсчёт
        Map<Status, Long> map = new HashMap<>();
        map.put(Status.NEW, 0L);
        map.put(Status.ACTIVE, 0L);
        map.put(Status.DISABLED, 0L);
        for (UserDto user : users) {
            if (user.getStatus().equals(Status.DISABLED)) {
                map.merge(Status.DISABLED, 1L,Long::sum);
            }
            if (user.getStatus().equals(Status.NEW)) {
                map.merge(Status.NEW, 1L,Long::sum);
            }
            if (user.getStatus().equals(Status.ACTIVE)) {
                map.merge(Status.ACTIVE, 1L,Long::sum);
            }
        }
        return map;

    }

    public double averageAge() {
        // Stream API: mapToInt + average
        double avg = 0;
        int i =0;
        for  (UserDto user : users) {
            avg += user.getAge();
            i++;
        }
        if (users.isEmpty()) {
            return 0.0d;
        }
        return avg/i;

    }

    public Optional<UserDto> findById(long id) {
        for (UserDto user : users) {
            if (user.getId() == id) {
                return Optional.of(user);
            }
        }
        return Optional.empty(); 
    }
}