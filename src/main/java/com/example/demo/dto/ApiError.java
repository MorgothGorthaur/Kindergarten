package com.example.demo.dto;

import java.util.List;

public record ApiError(String message, List<String> debugMessage) {
}
