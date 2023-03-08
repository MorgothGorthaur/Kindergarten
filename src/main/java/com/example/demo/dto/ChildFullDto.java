package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;


public record ChildFullDto(String name, LocalDate birthYear, List<RelativeDto> relatives) {

}
