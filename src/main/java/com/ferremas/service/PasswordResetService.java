package com.ferremas.service;



import com.ferremas.model.PasswordResetToken;
import com.ferremas.model.Usuario;
import com.ferremas.repository.PasswordResetTokenRepository;
import com.ferremas.repository.UsuarioRepository;
import com.ferremas.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        String subject = "Recuperación de Contraseña";
        String message = "Haz clic en el siguiente enlace para restablecer tu contraseña: " + resetUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(correo);
        email.setSubject(subject);
        email.setText(message);

        try {
            mailSender.send(email);
        }catch (MailException ex){
            ex.printStackTrace();
            Logger.logInfo(ex.getMessage());
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
