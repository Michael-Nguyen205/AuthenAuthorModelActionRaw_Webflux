//package spring.boot.authenauthor.callback;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.relational.core.mapping.event.AfterDeleteCallback;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//import spring.boot.authenauthor.entities.Permission;
//@Log4j2
//@Component
//public class PermissionAfterDeleteCallback implements AfterDeleteCallback<Permission> {
//
//
//    @Override
//    public Permission onAfterDelete(Permission permission) {
//        // Thực hiện logic sau khi xóa entity Permission tại đây.
//        System.out.println("After delete callback executed for permission: " + permission);
//        log.error("After delete callback");
//        return permission;
//    }
//}
