package com.hungdoan.aquariux.service.spec;

import com.hungdoan.aquariux.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> login(String identifier, String password);
}
