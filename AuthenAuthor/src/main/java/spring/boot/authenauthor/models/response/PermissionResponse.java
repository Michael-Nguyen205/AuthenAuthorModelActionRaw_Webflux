package spring.boot.authenauthor.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PermissionResponse {


    private Integer id;
    private String name;

    @JsonProperty("Model")
    private List<ModelResponse> modelResponses;
}

