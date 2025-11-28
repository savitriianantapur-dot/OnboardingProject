package com.auth.userService;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.dto.UserDTO;
import com.auth.entity.UserEntity;
import com.auth.exception.UserNotFoundException;
import com.auth.repository.UserRepository;

@Service
public class UserRegService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<UserDTO> getAllUser() {
		List<UserEntity> list = userRepository.findAll();
		List<UserDTO> list1 = list.stream()
				.map(user -> new UserDTO(user.getId(), user.getUsername(), user.getRoles(), user.getEmail()))
				.collect(Collectors.toList());

		return list1;
	}

	public UserEntity getUserById(Long id) {

		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

	}

	public void deleteUserById(Long id) {

		userRepository.deleteById(id);
	}

//	public UserEntity updateUserName(Long id,Map<String, String> requestBody) {
//		
//		UserEntity existingUser =userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
//		existingUser.setUsername(requestBody);
//		return userRepository.save(existingUser);
//	}

	public UserEntity updateUserName(Long id, String newUserName) {
		UserEntity existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		existingUser.setUsername(newUserName);
		return userRepository.save(existingUser);

	}

	public UserEntity updateUserPassword(String email, String newPassword) {
		UserEntity existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
		existingUser.setPassword(passwordEncoder.encode(newPassword));
		return userRepository.save(existingUser);

	}

}
