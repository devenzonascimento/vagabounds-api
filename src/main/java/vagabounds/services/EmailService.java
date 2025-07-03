package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

@Service
public class EmailService {
    private static final String APP_NAME = "Vagabounds";

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // Quando candidato aplica
    public void sendAppliedEmail(String candidateEmail, String candidateName, String jobTitle) {
        String subject = String.format("Confirmação de candidatura - %s", jobTitle);
        String body = buildAppliedBody(candidateName, jobTitle);
        sendEmail(candidateEmail, subject, body);
    }

    // Quando candidatura aprovada
    public void sendApprovedEmail(String candidateEmail, String candidateName, String jobTitle) {
        String subject = String.format("Parabéns! Você foi aprovado - %s", jobTitle);
        String body = buildApprovedBody(candidateName, jobTitle);
        sendEmail(candidateEmail, subject, body);
    }

    // Quando candidatura rejeitada manualmente
    public void sendRejectedEmail(String candidateEmail, String candidateName, String jobTitle, String feedback) {
        String subject = String.format("Atualização de candidatura - %s", jobTitle);
        String body = buildRejectedBody(candidateName, jobTitle, feedback);
        sendEmail(candidateEmail, subject, body);
    }

    // Quando candidatura auto-rejeitada por regra de negócio
    public void sendAutoRejectedEmail(String candidateEmail, String candidateName, String jobTitle, String reason) {
        String subject = String.format("Candidatura não avançou - %s", jobTitle);
        String body = buildAutoRejectedBody(candidateName, jobTitle, reason);
        sendEmail(candidateEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

    private String buildAppliedBody(String name, String job) {
        return String.format(
            "<p>Olá %s,</p>" +
                "<p>Recebemos sua candidatura para a vaga <strong>%s</strong>.</p>" +
                "<p>Nossa equipe está avaliando seu perfil e entraremos em contato em breve com os próximos passos.</p>" +
                "<p>Atenciosamente,<br/>%s Team</p>",
            name, job, APP_NAME
        );
    }

    private String buildApprovedBody(String name, String job) {
        return String.format(
            "<p>Olá %s,</p>" +
                "<p>Parabéns! Sua candidatura para a vaga <strong>%s</strong> foi <strong>aprovada</strong>.</p>" +
                "<p>Entraremos em contato em breve para lhe dar as proximas instruções.</p>" +
                "<p>Estamos ansiosos para tê-lo em nossa equipe!</p>" +
                "<p>Atenciosamente,<br/>%s Team</p>",
            name, job, APP_NAME
        );
    }

    private String buildRejectedBody(String name, String job, String feedback) {
        return String.format(
            "<p>Olá %s,</p>" +
                "<p>Agradecemos seu interesse pela vaga <strong>%s</strong>.</p>" +
                "<p>Após análise, infelizmente não avançaremos com sua candidatura.</p>" +
                "<p>Feedback: %s</p>" +
                "<p>Desejamos sucesso em suas próximas oportunidades.</p>" +
                "<p>Atenciosamente,<br/>%s Team</p>",
            name, job, feedback, APP_NAME
        );
    }

    private String buildAutoRejectedBody(String name, String job, String reason) {
        return String.format(
            "<p>Olá %s,</p>" +
                "<p>Sua candidatura para a vaga <strong>%s</strong> não atende aos requisitos necessários.</p>" +
                "<p>Motivo: %s</p>" +
                "<p>Você pode visualizar outras vagas em <a href='%s'>nosso site</a>.</p>" +
                "<p>Atenciosamente,<br/>%s Team</p>",
            name, job, reason, "https://vagabounds.com", APP_NAME
        );
    }
}
