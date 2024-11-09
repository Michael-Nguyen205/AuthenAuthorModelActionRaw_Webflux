package spring.boot.authenauthor.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;


@RequiredArgsConstructor
@Log4j2
@Component
public class EntityCascadeDeletionUtil
//        <T, ID extends Serializable>
{
    private final DatabaseClient databaseClient;



    public Mono<Void> deleteByParentId(Integer parentId, String parentColumnName) {
        return databaseClient.sql("SELECT table_name FROM information_schema.columns "
                        + "WHERE column_name = '" + parentColumnName + "' AND table_schema = 'WebCoCo'")
                .map(row -> row.get("table_name", String.class))
                .all()
                .flatMap(tableName -> databaseClient.sql("DELETE FROM " + tableName + " WHERE " + parentColumnName + " = " + parentId)
                        .fetch()
                        .rowsUpdated())
                .then();
    }









//    public Mono<Void> deleteByParentId(Integer parentId, String parentColumnName) {
//        return databaseClient.sql("SELECT table_name FROM information_schema.columns "
//                        + "WHERE column_name = :parentColumnName AND table_schema = 'WebCoCo'")
//                .bind("parentColumnName", parentColumnName)
//                .map(row -> row.get("table_name", String.class))
//                .all()
//                .flatMap(tableName -> databaseClient.sql("DELETE FROM " + tableName + " WHERE " + parentColumnName + " = :parentId")
//                        .bind("parentId", parentId)
//                        .fetch()
//                        .rowsUpdated())
//                .then();
//    }


    public Mono<Void> deleteByEntityParentIdOnTable(Integer permissionId, String tableName) {
        if (permissionId == null) {
        throw new AppException(ErrorCode.DATA_NOT_FOUND,"khoong tim thay permissionId ");
        }
        String query = "DELETE FROM " + tableName + " WHERE permission_id = " + permissionId;
        log.error("query: {}",query);
        String queryy = "SHOW TABLES";
        return databaseClient.sql(query)
//                .onErrorMap(e -> new AppException(ErrorCode.ERROR_GENERATE_TOKEN, "Lỗi tạo token"))
                .fetch()
                .rowsUpdated()
                .flatMap(rowsAffected -> {
                    if (rowsAffected > 0) {
                        log.error("Rows affected: {}", rowsAffected);
                        log.error("Rows affected: {}", rowsAffected);
                        return Mono.empty();
                    }else{
                        return Mono.error(new AppException(ErrorCode.DATABASE_DELETE_ERROR, "có db"));
                    }
                });

    }




//    public Mono<Void> cascadeDeletion(  List<Enum> entityRelationships  , ID DeletionId, ReactiveCrudRepository<T, ID> repository) {
//
//        log.error("DeletionId: {}",DeletionId);
//        log.error("DeletionId: {}",entityRelationships);
//
//
//        return null;
//    }
//

}
