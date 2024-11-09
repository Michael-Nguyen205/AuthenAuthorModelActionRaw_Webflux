package spring.boot.authenauthor.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorizeResponse {


    @JsonProperty("Permisson")
    private PermissionResponse permissonResponse;
}
