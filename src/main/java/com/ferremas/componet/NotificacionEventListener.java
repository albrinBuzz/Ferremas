package com.ferremas.componet;

import com.ferremas.config.dataBase.PgNotifyConfig;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.Executors;

@Service
public class NotificacionEventListener {

    @Autowired
    private PgNotifyConfig config;

    @Autowired
    private JavaMailSender mailSender;

    @PostConstruct
    public void startListener() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                // Conexión directa para manejar el protocolo de notificaciones de Postgres
                Connection conn = DriverManager.getConnection(
                        config.getJdbcUrl(),
                        config.getUsername(),
                        config.getPassword()
                );

                PGConnection pgconn = conn.unwrap(PGConnection.class);

                try (Statement stmt = conn.createStatement()) {
                    // Usamos el canal que definimos en el trigger mejorado
                    stmt.execute("LISTEN canal_notificaciones");
                }

                Logger.logInfo("📡 FERREMAS: Escuchando eventos en 'canal_notificaciones'...");

                while (true) {
                    // Consultamos si hay notificaciones pendientes en el socket
                    PGNotification[] notifications = pgconn.getNotifications();

                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            String idNotificacion = notification.getParameter();
                            procesarNotificacionFerremas(conn, idNotificacion);
                        }
                    }

                    // Pausa de cortesía y Keep-Alive
                    Thread.sleep(1000);
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("SELECT 1");
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Error en Listener FERREMAS: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void procesarNotificacionFerremas(Connection conn, String idNotificacion) {
        // SQL adaptado para buscar en USUARIO o ClienteInvitado
        String query = """
            SELECT n.id_notificacion, n.titulo, n.mensaje, n.tipo,
                   COALESCE(u.correo, ci.correo) as correo_destino
            FROM Notificacion n
            LEFT JOIN USUARIO u ON u.rut_usuario = n.rut_receptor
            LEFT JOIN ClienteInvitado ci ON ci.rutCliente = n.rut_receptor
            WHERE n.id_notificacion = ? AND n.leido = FALSE
        """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(idNotificacion));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String email = rs.getString("correo_destino");
                String titulo = rs.getString("titulo");
                String mensaje = rs.getString("mensaje");
                String tipo = rs.getString("tipo");

                enviarEmailCorporativo(email, titulo, mensaje, tipo);

                // Marcar como leída/enviada
                try (PreparedStatement update = conn.prepareStatement(
                        "UPDATE Notificacion SET leido = TRUE WHERE id_notificacion = ?"
                )) {
                    update.setInt(1, Integer.parseInt(idNotificacion));
                    update.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al procesar Email: " + e.getMessage());
        }
    }

    private void enviarEmailCorporativo(String email, String titulo, String mensaje, String tipo) throws Exception {
        if (email == null || email.isEmpty()) return;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("ferremas_notificaciones@ferremas.cl");
        helper.setTo(email);
        helper.setSubject("FERREMAS: " + titulo);

        // Definición de icono y color según el estado (opcional para feedback visual)
        String colorPrimario = "#fbbf24"; // Amarillo Ferremas
        String statusIcon = "🔔";

        if (titulo.contains("Aprobado") || titulo.contains("Validado")) {
            statusIcon = "✅";
        } else if (titulo.contains("Rechazado") || titulo.contains("Cancelado")) {
            statusIcon = "❌";
            colorPrimario = "#ef4444"; // Rojo para alertas
        }

        String html = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body style="margin:0; padding:0; background-color:#f1f5f9; font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">
        <div style="width:100%%; background-color:#f1f5f9; padding: 40px 0;">
            <div style="max-width:600px; margin:0 auto; background-color:#ffffff; border-radius:16px; overflow:hidden; shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
                
                <div style="background-color:#161b22; padding:30px; text-align:center; border-bottom: 5px solid %s;">
                    <h1 style="color:#ffffff; margin:0; font-size:28px; letter-spacing:2px; font-weight:900;">
                        FERREMAS<span style="color:%s;">PRO</span>
                    </h1>
                </div>

                <div style="padding:40px; background-color:#ffffff;">
                    <div style="text-align:center; margin-bottom:25px;">
                        <span style="font-size:50px;">%s</span>
                    </div>
                    
                    <h2 style="color:#1e293b; margin-top:0; font-size:22px; text-align:center;">%s</h2>
                    
                    <div style="margin:25px 0; padding:20px; background-color:#f8fafc; border-radius:12px; border:1px solid #e2e8f0;">
                        <p style="font-size:16px; line-height:1.6; color:#475569; margin:0;">
                            %s
                        </p>
                    </div>

                    <table style="width:100%%; border-collapse:collapse; margin-bottom:30px;">
                        <tr>
                            <td style="padding:10px 0; color:#64748b; font-size:13px; border-bottom:1px solid #f1f5f9;"><b>Categoría:</b></td>
                            <td style="padding:10px 0; color:#1e293b; font-size:13px; border-bottom:1px solid #f1f5f9; text-align:right;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding:10px 0; color:#64748b; font-size:13px;"><b>Fecha de emisión:</b></td>
                            <td style="padding:10px 0; color:#1e293b; font-size:13px; text-align:right;">%s</td>
                        </tr>
                    </table>

                    <div style="text-align:center;">
                        <a href="http://tu-web-ferremas.com/mis-pedidos" 
                           style="background-color:#161b22; color:#fbbf24; padding:16px 32px; text-decoration:none; 
                                  font-weight:bold; border-radius:8px; display:inline-block; font-size:14px; text-transform:uppercase;">
                            Ver Detalles en el Portal
                        </a>
                    </div>
                </div>

                <div style="background-color:#f8fafc; padding:25px; text-align:center; border-top:1px solid #e2e8f0;">
                    <p style="color:#94a3b8; font-size:12px; margin:0; line-height:1.5;">
                        <b>Suministros Ferremas S.A.</b><br>
                        Calidad Industrial y Construcción Profesional<br>
                        <span style="color:#cbd5e1; font-size:10px;">Este es un mensaje generado automáticamente, por favor no responda a este correo.</span>
                    </p>
                </div>
            </div>
            
            <div style="text-align:center; margin-top:20px;">
                <p style="color:#94a3b8; font-size:11px;">&copy; 2026 Ferremas S.A. Todos los derechos reservados.</p>
            </div>
        </div>
    </body>
    </html>
    """.formatted(colorPrimario, colorPrimario, statusIcon, titulo, mensaje, tipo, new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));

        helper.setText(html, true);
        mailSender.send(mimeMessage);
        Logger.logInfo("✅ Correo de " + tipo + " enviado exitosamente a " + email);
    }
}