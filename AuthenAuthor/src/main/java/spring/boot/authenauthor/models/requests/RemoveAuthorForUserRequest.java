package spring.boot.authenauthor.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveAuthorForUserRequest {


    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("permission_id")
    private Integer permissionId;


}
