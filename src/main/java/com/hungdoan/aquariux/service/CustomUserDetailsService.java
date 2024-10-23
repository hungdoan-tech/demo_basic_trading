package com.hungdoan.aquariux.service;

import com.hungdoan.aquariux.data_access.spec.UserRepository;
import com.hungdoan.aquariux.dto.CustomUserDetails;
import com.hungdoan.aquariux.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Map<String, User> cache = new HashMap<>();

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = cache.get(username);
        if (user == null) {

            Optional<User> loadedUser;
            if (username.contains("@")) {
                loadedUser = userRepository.findByEmail(username);
            } else {
                loadedUser = userRepository.findByUsername(username);
            }

            if (loadedUser.isEmpty()) {
                throw new UsernameNotFoundException("Can not find username claim of jwt " + username);
            }

            cache.put(username, loadedUser.get());
            user = loadedUser.get();
        }
        return new CustomUserDetails(user);
    }
}
