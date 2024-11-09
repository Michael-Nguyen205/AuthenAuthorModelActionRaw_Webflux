package spring.boot.authenauthor.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Models;
import spring.boot.authenauthor.entities.Permission;

import java.util.List;

public interface PermissionRepository extends BaseRepository<Permission, Integer> {
    Flux<Permission> findByIdIn(List<Integer> id);









//    @Query(value = "SET @permission_id = ?1; " +
//            "SET @sql = ''; " +
//            "SELECT GROUP_CONCAT(CONCAT('DELETE FROM ', table_name, ' WHERE permission_id = ', @permission_id, ';') SEPARATOR ' ') INTO @sql " +
//            "FROM information_schema.columns " +
//            "WHERE column_name = 'permission_id' " +
//            "AND table_schema = 'WebCoCo'; " +
//            "PREPARE stmt FROM @sql; " +
//            "EXECUTE stmt; " +
//            "DEALLOCATE PREPARE stmt;")
//    Mono<Void> deleteByPermissionId(Integer permissionId);





//
//
//    DELIMITER //
//    SET @permission_id = 5;
//    SET @sql = '';
//    SELECT
//    GROUP_CONCAT(CONCAT('DELETE FROM ', table_name, ' WHERE permission_id = ', @permission_id) SEPARATOR '; ') INTO @sql
//    FROM information_schema.columns
//    WHERE column_name = 'permission_id'
//    AND table_schema = 'WebCoCo';
//    PREPARE stmt FROM @sql;
//    EXECUTE stmt;
//    DEALLOCATE PREPARE stmt;
////
//    DELIMITER ;
//
//








}
