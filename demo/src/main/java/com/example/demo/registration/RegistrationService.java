package com.example.demo.registration;


import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserRole;
import com.example.demo.webuser.WebUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class RegistrationService {


    private final WebUserService webUserService;
    private final ConfirmationTokenService confirmationTokenService;


    public ArrayList<String> register(RegistrationRequest request) {
        ArrayList<String> token = webUserService.signUpUser(
                new WebUser(
                        request.getPassword(),
                        request.getEmail(),
                        request.getUsername(),
                        WebUserRole.USER
                )
        );
        /*webUserService.enableWebUser(
                request.getEmail()
        );*/

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        webUserService.enableWebUser(
                confirmationToken.getWebUser().getEmail()
        );

        return "confirmed";
    }


}
