package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:Vagabounds}")
    private String appName;

    public void sendEmailToConfirmApplication() {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper emailHelper = new MimeMessageHelper(message, true, "UTF-8");

            emailHelper.setFrom(fromEmail);
            emailHelper.setTo("email teste");
            emailHelper.setSubject("Confirmação de Aplicação para a vaga: GoHorse");
            emailHelper.setText("so um teste mesmo", true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to confirm application.", e);
        }
    }

}
