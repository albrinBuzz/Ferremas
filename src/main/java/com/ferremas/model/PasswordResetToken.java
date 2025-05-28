package com.ferremas.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24; // en minutos

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "password_reset_tokens_seq")
    @SequenceGenerator(name = "password_reset_tokens_seq",sequenceName = "password_reset_tokens_seq",allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "rutUsuario", nullable = false)
    private Usuario user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    // (Opcional) Constructor, utilidad para calcular fecha de expiración

    public PasswordResetToken() {}

    public PasswordResetToken(String token, Usuario user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private LocalDateTime calculateExpiryDate(int minutes) {
        LocalDateTime now = LocalDateTime.now();  // Fecha y hora actual
        return now.plus(minutes, ChronoUnit.MINUTES); // Sumar los minutos
    }

    public Usuario getUser() {
        return user;
    }



    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PasswordResetToken{");
        sb.append("id=").append(id);
        sb.append(", token='").append(token).append('\'');
        sb.append(", user=").append(user);
        sb.append(", expiryDate=").append(expiryDate);
        sb.append('}');
        return sb.toString();
    }
}
