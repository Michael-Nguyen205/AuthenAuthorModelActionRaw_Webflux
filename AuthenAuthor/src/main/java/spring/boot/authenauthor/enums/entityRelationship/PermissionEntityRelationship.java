package spring.boot.authenauthor.enums.entityRelationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PermissionEntityRelationship {
    USER_PERMISSION("user_permission"),
    PERMISSION_MODEL_ACTION_RAW("permission_model_action_raw");

    private  String entityName;
}
