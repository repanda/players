package com.example.demo.infrastructure.api;

import java.util.function.Predicate;

public interface Publisher {

    String getPayload();

    Predicate<String> getFilters();
}
