package spring.boot.authenauthor.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import spring.boot.authenauthor.models.requests.CreateActionRequest;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ModelResponse {
    @JsonProperty("model_id")
    private Integer modelId;

    @JsonProperty("model_name")
    private String modelName;

    @JsonProperty("model_description")
    private String modelDescription;

    @JsonProperty("Action")
    private List<ActionResponse> actionResponses;


}
