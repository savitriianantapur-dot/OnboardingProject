package com.auth.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
//import jakarta.mail.internet.MimeMessage;


@Service
public class EmailNotificationService {

	 @Autowired
	    private JavaMailSender mailSender;

	 @Async
	    public void sendMail(String to, String name) {
	        try {
	            //SimpleMailMessage message = new SimpleMailMessage();
	        	
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            helper.setFrom("manage.dextris@gmail.com"); // must match your mail.username
	            helper.setTo(to);
	            helper.setSubject("Welcome to Dextris – Your account details inside");
	            String htmlBody = """
	    	            <html>
	    	            <body style="font-family: Arial, sans-serif; background-color: #f5f6f8; padding: 20px;">
	    	                <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
	    	                    <div style="background-color: #007bff; color: white; padding: 15px; border-radius: 8px 8px 0 0;">
	    	                        <h2 style="margin: 0;">Welcome to Dextris</h2>
	    	                    </div>
	    	                    <div style="padding: 20px; color: #333;">
	    	                        <p>Hi <strong>%s</strong>,</p>
	    	                        <p>Welcome to <strong>Dextris</strong>! We're excited to have you on board.</p>
	    	                        <p>Here are your login details:</p>
	    	                        <table style="border-collapse: collapse; width: 100%%; margin-top: 10px;">
	    	                            <tr>
	    	                                <td style="padding: 8px; font-weight: bold;">Login Mail:</td>
	    	                                <td style="padding: 8px; background-color: #f1f1f1;">%s</td>
	    	                            </tr>
	    	                            <tr>
	    	                                <td style="padding: 8px; font-weight: bold;">Default Password:</td>
	    	                                <td style="padding: 8px; background-color: #f1f1f1;">%s</td>
	    	                            </tr>
	    	                        </table>
	    	                        <p style="margin-top: 20px;">Please log in and change your password after your first sign-in for security reasons.</p>
	    	                      </br></br>
	    	                        <p style="margin-top: 30px; font-size: 12px; color: #666;">If you didn’t request this, please ignore this email.</p>
	    	                    </div>
	    	                </div>
	    	            </body>
	    	            </html>
	    	            """.formatted(name, to, "Password@123");

	            helper.setText(htmlBody,true);
	            mailSender.send(message);
	           // mailSender.send(message);

	            System.out.println("Mail sent successfully to " + to);
	        } catch (Exception e) {
	            System.out.println("Error sending email to " + to + ": " + e.getMessage());
	        }
	    }
	    }
