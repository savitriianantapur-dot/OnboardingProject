package com.auth.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth.dto.UserDTO;
import com.auth.entity.UserEntity;
import com.auth.userService.AdminService;
import com.auth.userService.UserRegService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserRegService userRegDetails;
	
	@Autowired
	private AdminService adminService;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin dashboard - only for ADMIN role";
    }
    
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRegDetails.getAllUser();
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
    	userRegDetails.deleteUserById(id);
    	return ResponseEntity.noContent().build();
    	}
    @PatchMapping("/updateById/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id,
    		                                     @RequestBody Map<String,String> requestBody) {
    	String newUserName=requestBody.get("username");
    	UserEntity user=userRegDetails.updateUserName(id,newUserName);
    	
		return ResponseEntity.ok(user);
    	
    }
    @PostMapping("/upload-users")
    public ResponseEntity<String> storeBulkUsers(@RequestParam MultipartFile file) {
    	String resp = adminService.loadToDbUsers(file);
		return ResponseEntity.ok(resp);
    	
    }
    	
    	
    		
    		
    		
    	}
    	
    
    
    

