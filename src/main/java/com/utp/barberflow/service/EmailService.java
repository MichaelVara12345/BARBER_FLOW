package com.utp.barberflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
@Async
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(destinatario);
            email.setSubject(asunto);
            email.setText(mensaje);
            
            mailSender.send(email);
            System.out.println("Correo enviado exitosamente a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo a " + destinatario + ": " + e.getMessage());
        }
    }
}