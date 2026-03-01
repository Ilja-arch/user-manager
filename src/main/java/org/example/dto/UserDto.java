package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class UserDto {
    private final long id;
    private final String name;
    private final int age;
    private final Status status;
}