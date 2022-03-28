package com.example.demo.webuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class WebUserService implements UserDetailsService{

    private final WebUserRepository webUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
   // private final AuthenticationManager authenticationManager;
    //private final JwtUtil jwtUtil;
    private final static String USER_NOT_FOUND = "username %s not found";

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return webUserRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public JSONObject signUpUser(WebUser webUser) {
        Boolean usernameExists = webUserRepository.findByUsername(webUser.getUsername()).isPresent();
        Boolean emailExists = webUserRepository.findByEmail(webUser.getEmail()).isPresent();
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

    public void updateFav(WebUser webUser, boolean isAdd, Long jobId) throws Exception {

        try {
            if(isAdd) {
                if(!webUser.getFavList().contains(jobId)) {
                    webUser.getFavList().add(jobId);
                    em.merge(webUser);
                }

            }
            else {
                webUser.getFavList().remove(jobId);
                em.merge(webUser);
            }
        }catch (Exception e) {
            log.error("Unable to update favorite list");
            throw new Exception(e);
        }

    }
}
