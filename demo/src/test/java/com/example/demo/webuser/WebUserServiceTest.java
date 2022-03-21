package com.example.demo.webuser;

import com.example.demo.job.JobRepository;
import com.example.demo.job.JobService;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import com.example.demo.security.util.JwtUtil;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebUserServiceTest {

    private WebUserService webUserService;
    private WebUser webUser = new WebUser();
    private ConfirmationToken confirmationToken = new ConfirmationToken();

    @Mock
    private WebUserRepository webUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ConfirmationTokenService confirmationTokenService;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        //jobRepository = Mockito.mock(JobRepository.class);
        webUserService = new WebUserService(webUserRepository,bCryptPasswordEncoder,confirmationTokenService);
        Mockito.when(webUserRepository.findByUsername("U1")).thenReturn(Optional.of(new WebUser()));
        Mockito.when(webUserRepository.findByEmail("E1")).thenReturn(Optional.of(new WebUser()));
        Mockito.when(webUserRepository.findByUsername("U2")).thenReturn(Optional.empty());
        Mockito.when(webUserRepository.findByEmail("E2")).thenReturn(Optional.empty());
        Mockito.when(bCryptPasswordEncoder.encode("1")).thenReturn("1");
        Mockito.when(webUserRepository.save(webUser)).thenReturn(new WebUser());
        Mockito.doNothing().when(confirmationTokenService).saveConfirmationToken(confirmationToken);

    }

    @Test
    void signUpUserFailed() {
        webUser.setUsername("U1");
        webUser.setEmail("E1");
        JSONObject jsonObject = new JSONObject();
        ArrayList<Boolean> isRegistered = new ArrayList<>();
        isRegistered.add(false);
        isRegistered.add(false);
        jsonObject.put("username", webUser.getUsername());
        jsonObject.put("email", webUser.getEmail());
        jsonObject.put("isRegistered", isRegistered);

        JSONObject res = webUserService.signUpUser(webUser);
        assertEquals(jsonObject, res);
    }

    @Test
    void signUpUserSucceed() {
        webUser.setUsername("U2");
        webUser.setEmail("E2");
        JSONObject jsonObject = new JSONObject();
        ArrayList<Boolean> isRegistered = new ArrayList<>();
        isRegistered.add(true);
        isRegistered.add(true);
        jsonObject.put("username", webUser.getUsername());
        jsonObject.put("email", webUser.getEmail());
        jsonObject.put("isRegistered", isRegistered);

        JSONObject res = webUserService.signUpUser(webUser);
        assertEquals(jsonObject, res);
    }

}