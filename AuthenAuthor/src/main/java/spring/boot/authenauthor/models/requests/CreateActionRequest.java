package spring.boot.authenauthor.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateActionRequest {

    @JsonProperty("action_id")
    private Integer actionId;

    @JsonProperty("action_name")
    private String name;

    @JsonProperty("action_description")
    private String description;



    @JsonProperty("raw_id_list")
    private List<CreateRawRequest> rawRequests ;
}
