package spring.boot.authenauthor.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permission_model_action_raw")
public class PermissionActionModelRaw extends BaseEntity {

    @Id
    private String id;

    @Column("model_id")
    private Integer modelId;

    @Column("permission_id")
    private Integer permissionId;

    @Column("action_id")
    private Integer actionId;

    @Column("raw_id")
    private Integer rawId;

    // Nếu bạn muốn tạo UUID mới khi khởi tạo

}
