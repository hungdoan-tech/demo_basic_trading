package com.hungdoan.aquariux.data_access.spec;


import com.hungdoan.aquariux.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
