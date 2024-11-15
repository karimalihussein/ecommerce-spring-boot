package training.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String FailureMessage;
    private Boolean success = false;
}
