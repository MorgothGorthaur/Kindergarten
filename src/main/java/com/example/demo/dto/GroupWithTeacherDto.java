package com.example.demo.dto;

public record GroupWithTeacherDto(String name, int maxSize, int currentSize, TeacherDto teacher) {
}
