package com.ecommerce.services;


import com.ecommerce.Exception.EmailFailureException;
import com.ecommerce.model.VerificationToken;
import com.ecommerce.model.localuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailService {

    @Value("${email.from}")
        private  String fromAddress;

    @Value("${app.frontend.url}")
    private String url;
    @Autowired
    private JavaMailSender javaMailSender;
    public SimpleMailMessage sendemail()throws  EmailFailureException{
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return  simpleMailMessage;
    }
    public void sendverificationtoken(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage message = sendemail();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("verify your email to active your account");
        message.setText("please follow the link below to verify your email to active your account.\n"+url
                +"/auth/verify?token  =  "+verificationToken.getToken());
try{
    javaMailSender.send(message);

}catch (MailException ex){
throw new EmailFailureException();
}
    }
    public void sendResetPasswordEmailMassagesend(localuser user, String token) throws EmailFailureException {
        SimpleMailMessage message = sendemail();
        message.setTo(user.getEmail());
        message.setSubject("your password reset link");
        message.setText("you requested a password reset on our website .please"+"find the link below to be able to reset your password.\n"+url+
                "/auth/reset?token"+token);
        try{
            javaMailSender.send(message);
        }catch (MailException ex){
            throw new EmailFailureException();

        }


    }



}
