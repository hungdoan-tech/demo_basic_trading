package com.hungdoan.aquariux.transport;

import com.hungdoan.aquariux.common.key.JwtProvider;
import com.hungdoan.aquariux.dto.api.login.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetail = (UserDetails) authenticate.getPrincipal();
        String newAccessToken = jwtProvider.generateToken(userDetail);
        return ResponseEntity.ok(newAccessToken);
    }
}
