package com.expensemanager.expense_backend.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
    
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            logger.info("Sending email with template '{}' to: {}", templateName, to);
            logger.debug("Email variables: {}", variables);
            
            // Validate template name and build full path
            String templatePath = "email/" + templateName;
            logger.debug("Using template path: {}", templatePath);
            
            // Create the Thymeleaf context and add variables
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            
            // Process the template to get HTML content
            try {
                String htmlContent = templateEngine.process(templatePath, context);
                
                if (htmlContent == null || htmlContent.isEmpty()) {
                    logger.error("Template produced empty content: {}", templatePath);
                    throw new RuntimeException("Email template produced empty content");
                }
                
                // Create and send the email
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);  // true means it's HTML content
                
                mailSender.send(message);
                logger.info("Email sent successfully to {}", to);
            } catch (TemplateInputException e) {
                logger.error("Template parsing error for '{}': {}", templatePath, e.getMessage(), e);
                throw new RuntimeException("Failed to parse email template: " + e.getMessage(), e);
            }
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}