package spring.boot.authenauthor.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "UsernName cannot be blank")
    private String usernName;

    private String name;

    @NotBlank(message = "Password cannot be blank")
    private String password;




    private List<String> roles;

}