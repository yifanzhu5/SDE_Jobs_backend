package com.example.demo.webuser;

import com.example.demo.registration.RegistrationRequest;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WebUserService implements UserDetailsService{

    private final WebUserRepository webUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final static String USER_NOT_FOUND = "user with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webUserRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public ArrayList<String> signUpUser(WebUser webUser) {
        ArrayList<String> errmsg = new ArrayList<String>();
        boolean emailExists = webUserRepository.findByEmail(webUser.getEmail())
                .isPresent();
        if(emailExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            String msg = "email fail";
            errmsg.add(msg);
        }
        boolean userExists = webUserRepository.findByUsername(webUser.getUsername())
                .isPresent();
        if(userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            String msg = "user fail";
            errmsg.add(msg);
        }
        if (!errmsg.isEmpty()){
            return errmsg;
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(webUser.getPassword());

        webUser.setPassword(encodedPassword);
        webUserRepository.save(webUser);

        String token = UUID.randomUUID().toString();
        // send confirmation token
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                webUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        ArrayList<String> msg = new ArrayList<String>();
        msg.add(token);
        return msg;
    }

    public int enableWebUser(String email) {
        return webUserRepository.enableWebUser(email);
    }
}
