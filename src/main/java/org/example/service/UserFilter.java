package org.example.service;

import org.example.dto.UserDto;

/**
 * Простой функциональный интерфейс
 * Нужен для демонстрации лямбда-функций.
 */
@FunctionalInterface
public interface UserFilter {
    boolean test(UserDto user);
}