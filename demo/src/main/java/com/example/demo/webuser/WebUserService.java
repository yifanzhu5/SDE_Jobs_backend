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
import java.util.UUID;

@Service
@AllArgsConstructor
public class WebUserService implements UserDetailsService{

    private final WebUserRepository webUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final static String USER_NOT_FOUND = "user with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return webUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(WebUser webUser) {
        boolean userExists = webUserRepository.findByEmail(webUser.getEmail())
                .isPresent();
        if(userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            throw new IllegalStateException("user already exists");
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

        return token;
    }

    public int enableWebUser(String email) {
        return webUserRepository.enableWebUser(email);
    }
}
