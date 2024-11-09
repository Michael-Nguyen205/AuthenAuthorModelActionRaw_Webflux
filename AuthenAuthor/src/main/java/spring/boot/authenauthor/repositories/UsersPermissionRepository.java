package spring.boot.authenauthor.repositories;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.userdetails.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.entities.UserPermission;

import java.util.List;

public interface UsersPermissionRepository extends BaseRepository<UserPermission, Integer> {
    Mono<UserPermission> findByUserIdAndPermissionId(String userId,Integer permissionId );


    Mono<Long> deleteUserPermissionByUserIdAndPermissionId(String userId,Integer permissionId );


//    @Query(value = "SELECT DISTINCT action_id FROM permission_action_raw WHERE permission_id = :permissionId", nativeQuery = true)
//    List<Integer> findActionIdsByPermissionId(@Param("permissionId")Integer permissionId);

//    @Autowired
//    DatabaseClient databaseClient = DatabaseClient.create(); // Tạo một instance DatabaseClient
//
//
//    // Phương thức tìm action_id theo permissionId
//    default Flux<Integer> findAllPermissionByUserId(Integer userId) {
//        return databaseClient
//                .sql("SELECT permission_id FROM user_permissions WHERE user_id = :userId")
//                .bind("userId", userId)
//                .map((row, metadata) -> row.get("permission_id", Integer.class))
//                .all();
//    }
//
//    @Query(value = "SELECT DISTINCT permission_id FROM users_permissions WHERE users_id = :UserId", nativeQuery = true)
//    List<Integer> findAllPermissionByUserId(@Param("UserId")Integer UserId);


    @Query("SELECT permission_id FROM user_permissions WHERE user_id = :userId")
    Flux<Long> findAllPermissionByUserId(String userId);

}

