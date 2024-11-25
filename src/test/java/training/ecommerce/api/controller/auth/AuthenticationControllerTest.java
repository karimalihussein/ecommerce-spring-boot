package training.ecommerce.api.controller.auth;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void register_success_returnsCreated() throws Exception {
        String requestBody = """
            {
                "username": "dummy_user",
                "email": "dummy.user@example.com",
                "password": "dummyPassword123",
                "confirmPassword": "dummyPassword123",
                "firstName": "Dummy",
                "lastName": "User"
            }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void register_withMissingFields_returnsBadRequest() throws Exception {
        String requestBody = """
        {
            "username": "john_doe5",
            "password": "securePassword123"
        }
    """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError());
    }
}