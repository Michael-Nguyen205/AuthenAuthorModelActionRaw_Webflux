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
@Table(name = "user_permissions")
public class UserPermission extends BaseEntity {


    private String id;

    @Column("user_id")
    private String userId;

    @Column("permission_id")
    private Integer permissionId;

    // Getters and Setters

    public UserPermission() {
        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
    }

}
