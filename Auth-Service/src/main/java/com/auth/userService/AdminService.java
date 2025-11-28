package com.auth.userService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auth.entity.UserEntity;
import com.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailNotificationService emailNotificationService;
	
	private final ExecutorService executor = Executors.newFixedThreadPool(5);

	public List<UserEntity> readExcel(MultipartFile file) {
		List<UserEntity> users = new ArrayList<>();
		log.info("entered into the readExcel and the file name is : "+file.getName());
		boolean check = file.getContentType().equals(
	            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		if(check) {
			try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
	            Sheet sheet = workbook.getSheetAt(0);
	            

	            Iterator<Row> rows = sheet.iterator();
	            boolean header = true;

	            while (rows.hasNext()) {
	                Row currentRow = rows.next();

	                if (header) { // skip header row
	                    header = false;
	                    continue;
	                }

	                UserEntity user = new UserEntity();
	                user.setUsername(currentRow.getCell(1).getStringCellValue());
	                user.setEmail(currentRow.getCell(2).getStringCellValue());
	                user.setPassword(passwordEncoder.encode("Password@123"));
	                user.setRoles("USER");

	                users.add(user);
	            }
	            return users;

	        } catch (IOException e) {
	            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
	        }

		} else {
			return users;
		}
		
	}
	@Transactional
	public String loadToDbUsers(MultipartFile file) {
		List<UserEntity> newUsers= readExcel(file);
		log.info("newUsers : {}",newUsers);
		if(newUsers.isEmpty()) {
			return "No data loaded";
		}
		userRepository.saveAllAndFlush(newUsers);
		 triggerMail(newUsers);
		return "Data loaded sucessfully and mail sent ";
	}
	
	public void triggerMail(List<UserEntity> newUsers) {
		
		  for (UserEntity user : newUsers) {
              executor.submit(() -> {
                  try {
                	  emailNotificationService.sendMail(user.getEmail(), user.getUsername());
                  } catch (Exception e) {
                      System.err.println("Failed to send email to " + user.getEmail() + ": " + e.getMessage());
                  }
              });
	}
	}
}
