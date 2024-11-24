package com.ecommerce.services;

import com.ecommerce.Exception.EmailFailureException;
import com.ecommerce.Exception.EmailNotFoundException;
import com.ecommerce.Exception.UserNotVerifiedException;
import com.ecommerce.Exception.userAlreadyExsistsException;
import com.ecommerce.api.model.JWTbody;
import com.ecommerce.api.model.PasswordResetBody;
import com.ecommerce.api.model.RegistrationBody;
import com.ecommerce.model.Dao.LocalUserDao;
import com.ecommerce.model.Dao.VerificationTokenDao;
import com.ecommerce.model.VerificationToken;
import com.ecommerce.model.localuser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class userService {

    public LocalUserDao localUserDao;
    public EncryptionService encryptionService;
    public JWTService jj;
    private EmailService emailService;
    private VerificationTokenDao verificationTokenDao;

//    private PasswordEncoder passwordEncoder;
    public  localuser registration(RegistrationBody registrationBody) throws userAlreadyExsistsException, EmailFailureException {
        if(localUserDao.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() ||
                localUserDao.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()){
            throw new userAlreadyExsistsException();
        };

        localuser user = new localuser();
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptpassword(registrationBody.getPassword()));
//       user.setPassword(passwordEncoder.encode(registrationBody.getPassword()));
          user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstname());
        user.setLastname(registrationBody.getLastname());
        VerificationToken verificationToken = createverificationtoken(user);
        emailService.sendverificationtoken(verificationToken);
        verificationTokenDao.save(verificationToken);
              return localUserDao.save(user);

    }
    public VerificationToken createverificationtoken(localuser user)
    {
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(jj.generateverificationjwt(user));
    verificationToken.setCreatetimestamp(new Timestamp(System.currentTimeMillis()));
    verificationToken.setUser(user);
    user.getVerificationTokens().add(verificationToken);
    return verificationToken;
    }
    public String loginjwt(JWTbody jwTbody) throws UserNotVerifiedException, EmailFailureException {
        Optional<localuser> aCase = localUserDao.findByUsernameIgnoreCase(jwTbody.getUsername());
        if(aCase.isPresent()){
            localuser user = aCase.get();
            if(encryptionService. verifypassword(jwTbody.getPassword(),user.getPassword())) {
                if(user.getEmailVerified()) {
                    return jj.generateJWT(user);
                }else{
                    List<VerificationToken> verificationTokenList = user.getVerificationTokens();
                boolean resend = verificationTokenList.size()  == 0 ||
                        verificationTokenList.get(0).getCreatetimestamp().before(new Timestamp(System.currentTimeMillis() -(60*60*1000)));

                if(resend){
                    VerificationToken verificationToken = createverificationtoken(user);
                    verificationTokenDao.save(verificationToken);
                    emailService.sendverificationtoken(verificationToken);
                }

                throw new UserNotVerifiedException(resend);
                }
                }
        }
        return  null;

    }
    @Transactional
    public  boolean verifyuser(String token) {
        Optional<VerificationToken> opToken = verificationTokenDao.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            localuser user = verificationToken.getUser();
            if (!user.getEmailVerified()) {
                user.setEmailVerified(true);
                localUserDao.save(user);
                verificationTokenDao.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<localuser> Opuser = localUserDao.findByEmailIgnoreCase(email);
        if (Opuser.isPresent()){
            localuser user = Opuser.get();
            String s = jj.generateResetVerificationPasswordjwt(user);
            emailService.sendResetPasswordEmailMassagesend(user,s);

        }else {
            throw new EmailNotFoundException();
        }
    }
    public String resetpassword(PasswordResetBody body){
        String email = jj.getResetpasswordemail(body.getToken());
        Optional<localuser> byEmailIgnoreCase = localUserDao.findByEmailIgnoreCase(email);
        if (byEmailIgnoreCase.isPresent()){
            localuser user = byEmailIgnoreCase.get();
            user.setPassword(encryptionService.encryptpassword(body.getPassword()));
            localUserDao.save(user);
               }


        return email;
    }
public boolean userHasPermissionToUser(localuser user,Long id){
        return Objects.equals(user.getId(), id);
                
}


}
