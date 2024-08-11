package pet_project.DiscussHub.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.security.AuthenticationService;

@SpringBootTest
@Transactional
public class AuthenticationServiceTest {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Test
    public void AuthenticationService_Register_ReturnAuthenticationResponse(){
        RegisterRequest request = new RegisterRequest(
                "nazarius_pr",
                "protsnazar2004@gmail.com",
                "1234Ab"
        );

        AuthenticationResponse response = this.authenticationService.register(request);

        System.out.println("Access token: " + response.getToken());
        Assertions.assertNotNull(response);
    }

    @Test
    public void AuthenticationService_Authenticate_ReturnAuthenticationResponse(){
        AuthenticationRequest request = new AuthenticationRequest(
                "john@mail.com",
                "1111"
        );

        AuthenticationResponse response = this.authenticationService.authenticate(request);

        System.out.println("Access token: " + response.getToken());
        Assertions.assertNotNull(response);
    }
}
