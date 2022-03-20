package com.example.demo.webuser;

import com.example.demo.job.entity.Job;
import com.example.demo.registration.RegistrationRequest;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WebUserService implements UserDetailsService{

    private final WebUserRepository webUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final static String USER_NOT_FOUND = "username %s not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webUserRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public JSONObject signUpUser(WebUser webUser) {
        boolean usernameExists = webUserRepository.findByUsername(webUser.getUsername()).isPresent();
        boolean emailExists = webUserRepository.findByEmail(webUser.getEmail()).isPresent();
        ArrayList<Boolean> flg = new ArrayList<>();
        flg.add(!usernameExists);
        flg.add(!emailExists);
        if(usernameExists || emailExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            return returnRegInfo(webUser, flg);
            //throw new IllegalStateException("user already exists");
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

        return returnRegInfo(webUser, flg);
    }

    public int enableWebUser(String email) {
        return webUserRepository.enableWebUser(email);
    }

    //return json after checking user information
    public JSONObject returnRegInfo(WebUser webUser, ArrayList<Boolean> isRegistered) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isRegistered", isRegistered);
        jsonObject.put("username", webUser.getUsername());
        jsonObject.put("email", webUser.getEmail());
        return jsonObject;
    }
}
