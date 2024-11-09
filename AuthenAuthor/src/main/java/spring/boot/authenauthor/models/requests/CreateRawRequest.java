package spring.boot.authenauthor.models.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateRawRequest {

    @JsonProperty("raw_id")
    private Integer rawId;
}
