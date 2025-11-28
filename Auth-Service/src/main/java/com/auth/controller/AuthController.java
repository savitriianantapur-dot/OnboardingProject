package com.auth.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth.entity.UserEntity;
import com.auth.repository.UserRepository;
import com.auth.service.JwtService;

@RestController
@RequestMapping("/api")
//@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String roles = body.get("roles"); // default role USER

        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles); // e.g., "ADMIN" or "USER" or "ADMIN,USER"
        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // load user details and generate token
        var userDetails = new org.springframework.security.core.userdetails.User(
        		email,
                userRepo.findByEmail(email).get().getPassword(),
                java.util.Collections.emptyList() // we won't use authorities here to generate token; JwtService generates from stored user later
        );

        // better to load full UserDetails from service:
//        userDetails = (org.springframework.security.core.userdetails.User)
//                new in.ashokit.service.MyUserDetailsService() {
//                }; // dummy placeholder - we'll instead fetch real details below

        // simpler: fetch user from repo and populate UserDetails
        var userEntity = userRepo.findByEmail(email).get();
        User userDetailsFromService = new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                java.util.Arrays.stream(userEntity.getRoles().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(r -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + r))
                        .toList()
        );

        String token =jwtService.generateToken(userDetailsFromService);
        return ResponseEntity.ok(Map.of("token", token));
    }
    
    @PostMapping("/test")
    public String test(@RequestParam MultipartFile file) {
    	String resp = file.getName()+"     "+file.getOriginalFilename()+"     "+file.getContentType();
    	return resp;
    }
}

