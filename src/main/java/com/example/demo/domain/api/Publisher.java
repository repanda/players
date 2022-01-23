package com.example.demo.domain.api;

import java.util.function.Predicate;

public interface Publisher {

    String getPayload();

    Predicate<Listener> getFilters();
}
