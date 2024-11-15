package spring.boot.authenauthor.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.PermissionActionModelRaw;
//import spring.boot.authenauthor.entities.PermissionActionRaw;


@Repository
public interface PermissionActionModelRawRepository extends BaseRepository<PermissionActionModelRaw,Long> {

    Flux<PermissionActionModelRaw> findByPermissionId(Integer permissionId);

    @Query("SELECT COUNT(pamr.id) FROM permission_model_action_raw pamr " +
            "JOIN user_permissions up ON pamr.permission_id = up.permission_id " +
            "JOIN users u ON up.user_id = u.id " +
            "JOIN models m ON pamr.model_id = m.id " +
            "JOIN actions a ON pamr.action_id = a.id " +
            "WHERE m.name = :modelName " +
            "AND a.name = :actionName " +
            "AND u.email = :userEmail")
    Mono<Long> countActionModelRawAuthors(String userEmail, String actionName, String modelName);


    @Query("SELECT COUNT(pamr.id) \" +\n" +
            "//            \"FROM permission_model_action_raw pamr \" +\n" +
            "//            \"JOIN model m ON pamr.model_id = m.id \" +\n" +
            "//            \"JOIN action a ON pamr.action_id = a.id \" +\n" +
            "//            \"JOIN users_permission up ON pamr.permission_id = up.permission_id \" +\n" +
            "//            \"JOIN users u ON up.user_id = u.id \" +\n" +
            "//            \"WHERE m.name = :model \" +\n" +
            "//            \"AND a.name = :action \" +\n" +
            "//            \"AND pamr.raw_id = :rawId \" +\n" +
            "//            \"AND u.name = :userName")
    Mono<Long> countActionModelRawAuthors(String userName,Integer rawId,String actionName,String modelName);






//    @Query(value = "SELECT COUNT(pamr.id) " +
//            "FROM permission_action_model_raw pamr " +
//            "JOIN model m ON pamr.model_id = m.id " +
//            "JOIN action a ON pamr.action_id = a.id " +
//            "JOIN users_permission up ON pamr.permission_id = up.permission_id " +
//            "JOIN users u ON up.user_id = u.id " +
//            "WHERE m.name = :model " +
//            "AND a.name = :action " +
//            "AND pamr.raw_id = :rawId " +
//            "AND u.name = :userName", nativeQuery = true)
//    Long countActionModelRawAuthors(@Param("userName") String userName,
//                                    @Param("rawId") Integer rawId,
//                                    @Param("action") String actionName,
//                                    @Param("model") String modelName);






    }
















//    @Query(value = "SELECT DISTINCT action_id FROM permission_action_raw WHERE permission_id = :permissionId", nativeQuery = true)
//    List<Integer> findActionIdsByPermissionId(@Param("permissionId")Integer permissionId);
//
//
//
//
//    @Query(value = "SELECT posts_id FROM permission_action_raw WHERE permission_id = :permissionId AND action_id = :actionId", nativeQuery = true)
//    List<Integer> findPostIdsByPermissionAndAction(@Param("permissionId") Integer permissionId, @Param("actionId") Integer actionId);
//


//    @Query(value = "SELECT COUNT(*) > 0 " +
//            "FROM permission_action_raw par " +
//            "JOIN actions a ON par.action_id = a.id " +
//            "WHERE par.permission_id = :permissionId " +
//            "AND par.posts_id = :postId " +
//            "AND a.name = :actionName", nativeQuery = true)



//}
//
//