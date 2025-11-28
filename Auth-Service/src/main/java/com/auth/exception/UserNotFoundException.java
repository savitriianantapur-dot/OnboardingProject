package com.auth.exception;

public class UserNotFoundException extends RuntimeException{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public  UserNotFoundException(Long id) {
    	 super("User not found with ID: " + id);
     }
	 public  UserNotFoundException(String email) {
    	 super("User not found with ID: " + email);
     }

}
