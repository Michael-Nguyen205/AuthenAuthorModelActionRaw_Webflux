package spring.boot.authenauthor.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRegisterRequest {
    @JsonProperty("username")
    private String usernName;

    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @NotBlank(message = "email")
    private String email;

    @NotNull(message = "Role ID is required")
    @JsonProperty("permission_id")
    private Integer permissionId;
}