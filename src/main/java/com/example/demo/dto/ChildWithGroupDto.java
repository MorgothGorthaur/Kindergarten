package com.example.demo.dto;

import java.time.LocalDate;

public record ChildWithGroupDto(String name, LocalDate birthYear, String groupName) {
}
