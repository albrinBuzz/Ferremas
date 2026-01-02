package com.ferremas.service;



import com.ferremas.model.PasswordResetToken;
import com.ferremas.model.Usuario;
import com.ferremas.repository.PasswordResetTokenRepository;
import com.ferremas.repository.UsuarioRepository;
import com.ferremas.util.Logger;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final int EXPIRATION_TIME = 60 * 24;  // Expira en 24 horas

    @Transactional
    public void sendResetPasswordEmail(String email) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findByCorreo(email);

        if (usuario==null){
            throw new RuntimeException("Usuario no encontrado");
        }

        // Generar un token de recuperación
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, usuario);
        passwordResetTokenRepository.save(resetToken);

        // Enviar el correo electrónico con el enlace de recuperación
        sendEmail(usuario.getCorreo(), token);
    }



    private void sendEmail(String correo, String token) {
        String resetUrl = "http://localhost:8080/home/olvideClave.xhtml?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(correo);
            helper.setSubject("🔑 Recuperación de Acceso - Ferremas");

            // Diseño HTML incrustado
            String htmlContent =
                    "<div style='background-color: #0b0e14; padding: 40px; font-family: sans-serif; color: #e6edf3;'>" +
                            "<div style='max-width: 500px; margin: 0 auto; background-color: #161b22; border: 1px solid #30363d; border-radius: 16px; overflow: hidden;'>" +
                            "<div style='background-color: #1c2128; padding: 30px; text-align: center; border-bottom: 2px solid #fbbf24;'>" +
                            "<h1 style='color: #ffffff; margin: 0; font-size: 24px;'>FERREMAS<span style='color: #fbbf24;'>.</span></h1>" +
                            "</div>" +
                            "<div style='padding: 40px; text-align: center;'>" +
                            "<div style='background-color: rgba(251, 191, 36, 0.1); width: 60px; height: 60px; border-radius: 50%; margin: 0 auto 20px; display: table;'>" +
                            "<span style='display: table-cell; vertical-align: middle; font-size: 30px;'>🔑</span>" +
                            "</div>" +
                            "<h2 style='color: #ffffff; margin-bottom: 10px; font-size: 20px;'>¿Solicitaste un cambio de clave?</h2>" +
                            "<p style='color: #8b949e; font-size: 14px; line-height: 1.6;'>Recibimos una solicitud para restablecer la contraseña de tu cuenta profesional. Si no fuiste tú, puedes ignorar este correo de forma segura.</p>" +
                            "<div style='margin: 30px 0;'>" +
                            "<a href='" + resetUrl + "' style='background-color: #fbbf24; color: #0b0e14; padding: 14px 28px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 14px; display: inline-block;'>RESTABLECER CONTRASEÑA</a>" +
                            "</div>" +
                            "<p style='color: #8b949e; font-size: 12px;'>O copia y pega este código de seguridad:</p>" +
                            "<div style='background: #0d1117; padding: 10px; border-radius: 6px; font-family: monospace; color: #fbbf24; font-size: 16px; border: 1px dashed #fbbf24;'>" + token + "</div>" +
                            "</div>" +
                            "<div style='background-color: #0d1117; padding: 20px; text-align: center; color: #484f58; font-size: 11px;'>" +
                            "© 2026 Ferremas Industrial - Chile<br/>Este enlace expira en 60 minutos." +
                            "</div>" +
                            "</div>" +
                            "</div>";

            helper.setText(htmlContent, true); // 'true' indica que es HTML
            mailSender.send(mimeMessage);

        } catch (MessagingException ex) {
            ex.printStackTrace();
            Logger.logInfo("Error enviando correo: " + ex.getMessage());
        }
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        // Validar el token
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByToken(token);

        if (resetToken.isEmpty()){
            Logger.logInfo("token no encontrado");
            return;
        }

        if (resetToken.get().getExpiryDate().isBefore(java.time.LocalDateTime.now())) {

            throw new Exception("Token expirado");
        }

        // Actualizar la contraseña
        Usuario usuario = resetToken.get().getUser();
        usuario.setContrasena(new BCryptPasswordEncoder().encode(newPassword));
        usuarioRepository.save(usuario);
        Logger.logInfo("usuario actualizado");

        // Eliminar el token después de usarlo
        passwordResetTokenRepository.delete(resetToken.get());
    }
}
