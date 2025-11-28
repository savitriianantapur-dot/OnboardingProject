package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.entity.UserEntity;
import com.auth.userService.UserRegService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRegService userRegDetails;

    @GetMapping("/profile")
    public String userProfile() {
    	
        return "User profile - for USER or ADMIN";
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
    	UserEntity user= userRegDetails.getUserById(id);
		return ResponseEntity.ok(user);
    	
    }
    
    @PatchMapping("/updatepassword")
    public ResponseEntity<UserEntity> updatePassword(@RequestHeader String email, @RequestHeader String password) {
    	UserEntity user= userRegDetails.updateUserPassword(email, password);
		return ResponseEntity.ok(user);
    	
    }
    
    
    
    
    
    
}

