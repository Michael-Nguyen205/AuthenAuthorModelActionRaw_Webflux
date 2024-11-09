package spring.boot.authenauthor.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginRequest {

    @NotBlank(message = "Email number is required")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Min(value = 1, message = "You must enter role's Id")
    @JsonProperty("role_id")
    private Long roleId;


}
