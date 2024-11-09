package spring.boot.authenauthor.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table(name = "permission_model")
public class PermissionModel extends BaseEntity {

    @Id
    private String id;

    @NotBlank
    @Column( "model_id")
    private Integer modelId;

    @NotBlank
    @Column( "permission_id")
    private Integer permissionId;

    public PermissionModel() {
        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
    }

}
