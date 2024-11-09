//package spring.boot.authenauthor.utils;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.dao.DataAccessException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//import spring.boot.authenauthor.entities.Action;
//import spring.boot.authenauthor.entities.Permission;
//import spring.boot.authenauthor.entities.Users;
//import spring.boot.authenauthor.repositories.*;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@RequiredArgsConstructor
//@Log4j2
//@Component
//public class AuthorUtils {
//    private final PermissionRepository permissionRepository;
//    private final PermissionActionRawRepository permissionActionRawRepository;
//    private final UsersPermissionRepository usersPermissionRepository;
//    private final UserRepository userRepository;
//    private final ActionsRepository actionsRepository;
//
//
//    public boolean hasAuthor(String action, String model, Integer rawId) {
//        // Thiết lập giá trị mặc định cho model và rawId nếu cần thiết
//        model = (model != null) ? model : "defaultModel"; // Giá trị mặc định cho model
//        rawId = (rawId != null) ? rawId : -1; // Giả sử -1 là giá trị mặc định
//
//        // Lấy thông tin người dùng từ SecurityContext
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//        String username = ((UserDetails) principal).getUsername();
//
//        // Kiểm tra các trường hợp khác nhau
//        try {
//            if (model == null && action == null) {
//                // Code cho trường hợp cả model và action đều null
//                return false; // Ví dụ, không có quyền nào nếu cả hai đều null
//            } else if (model != null && action == null) {
//                // Trường hợp chỉ có model
//                Long count = permissionActionRawRepository.countActionModelRawAuthors(username, rawId, null, model);
//                log.error("count{}:", count);
//                return count != null && count > 0;
//            } else if (model == null && action != null) {
//                // Trường hợp chỉ có action
//                Long count = permissionActionRawRepository.countActionModelRawAuthors(username, rawId, action, null);
//                log.error("count{}:", count);
//                return count != null && count > 0;
//            } else {
//                // Trường hợp có cả model và action
//                Long count = permissionActionRawRepository.countActionModelRawAuthors(username, rawId, action, model);
//                log.error("count{}:", count);
//                return count != null && count > 0;
//            }
//        } catch (DataAccessException e) {
//            // Xử lý lỗi cơ sở dữ liệu
//            log.error("Database error while checking permissions: ", e);
//            throw new RuntimeException("An error occurred while checking permissions.", e);
//        } catch (Exception e) {
//            // Xử lý lỗi chung
//            log.error("Unexpected error while checking permissions: ", e);
//            throw new RuntimeException("An unexpected error occurred while checking permissions.", e);
//        }
//    }

package spring.boot.authenauthor.utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.repositories.PermissionActionModelRawRepository;

@RequiredArgsConstructor
@Log4j2
@Component
public class AuthorUtils {
    private final PermissionActionModelRawRepository permissionActionRawRepository;



//
//
//
//    public Mono<Boolean> hasAuthor(String action, String model, Integer rawId) {
//        // Thiết lập giá trị mặc định cho model và rawId nếu cần thiết
//        final String Model = (model != null) ? model : "defaultModel";
//        final Integer RawId = (rawId != null) ? rawId : -1;
//        final String Action = (action != null) ? action : "defaultAction";
//
//        // Lấy thông tin người dùng từ SecurityContext
//        Mono<Authentication> authenticationMono = ReactiveSecurityContextHolder.getContext().map()
//
//
//        log.error("authenticationnnnnn",authentication);
//        Object principal = authentication.getPrincipal();
//        log.error("principal",principal);
//
//        final String username = ((UserDetails) principal).getUsername();
//
//        return Mono.defer(() -> {
//            try {
//
//                if (Model == null && Action == null) {
//                    return Mono.just(false); // Trả về Mono<Boolean> cho trường hợp này
//                } else if (Model != null && Action == null) {
//                    // Trường hợp chỉ có model
//                    return permissionActionRawRepository.countActionModelRawAuthors(username, RawId, null, Model)
//                            .map(result -> result > 0) .doOnSuccess(count -> log.info("Count for model: {}", count)); // Ghi lại số lượng
//                } else if (Model == null && Action != null) {
//                    // Trường hợp chỉ có action
//                    return permissionActionRawRepository.countActionModelRawAuthors(username, RawId, Action, null)
//                            .map(result -> result > 0).doOnSuccess(count -> log.info("Count for model: {}", count)); // Ghi lại số lượng
//                } else {
//                    // Trường hợp có cả model và action
//                    return permissionActionRawRepository.countActionModelRawAuthors(username, RawId, Action, Model)
//                            .map(result -> result > 0) .doOnSuccess(count -> log.info("Count for model: {}", count)); // Ghi lại số lượng
//                }
//            } catch (DataAccessException e) {
//                // Xử lý lỗi cơ sở dữ liệu
//                log.error("Database error while checking permissions: ", e);
//                return Mono.error(new RuntimeException("An error occurred while checking permissions.", e));
//            } catch (Exception e) {
//                // Xử lý lỗi chung
//                log.error("Unexpected error while checking permissions: ", e);
//                return Mono.error(new RuntimeException("An unexpected error occurred while checking permissions.", e));
//            }
//        });








        public Mono<Boolean> hasAuthor(String action, String model, Integer rawId) {
            // Thiết lập giá trị mặc định cho model và rawId nếu cần thiết
            return ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .flatMap(authentication -> {
                        if (authentication == null) {
                            log.error("Authentication is null");
                            return Mono.error(new AppException(ErrorCode.UNAUTHORIZED, "Authentication không hợp lệ"));
                        }
                        log.error("authenticationnnnnn: {}", authentication);
                        Object principal = authentication.getPrincipal();
                        log.error("principal: {}", principal);
                        if (!(principal instanceof UserDetails userDetails)) {
                            log.error("Principal is not an instance of UserDetails");
                            return Mono.error(new AppException(ErrorCode.UNAUTHORIZED, "Principal không hợp lệ"));
                        }
                        final String username = userDetails.getUsername();


                        if (username == null) {
                            log.error("Username is null in UserDetails");
                            return Mono.error(new AppException(ErrorCode.UNAUTHORIZED, "Username không hợp lệ"));
                        }
                        try {
                            log.error("tryyyyyyyyyyyyyy");
                            if (model == null && action == null) {
                                log.error("tryyyyyyyyyyyyyy 1111");
                                return Mono.just(false); // Trả về Mono<Boolean> cho trường hợp này
                            } else if (model != null && action == null && rawId==null) {
                                log.error("tryyyyyyyyyyyyyy 2222");
                                // Trường hợp chỉ có model
                                return permissionActionRawRepository.countActionModelRawAuthors(username, rawId, null, model)
                                        .map(result -> result > 0) .doOnSuccess(count -> log.info("Count for model: {}", count)); // Ghi lại số lượng
                            } else if (model != null && action != null && rawId==null) {
                                log.error("tryyyyyyyyyyyyyy 333");
                                log.error("check model và action");
                                log.error("Checking permissions for user: {}, action: {}, model: {}", username, action, model);
                                // Trường hợp chỉ có action và model
                                return permissionActionRawRepository.countActionModelRawAuthors(username, action, model)
                                        .onErrorResume( e -> {
                                            log.error("tryyyyyyyyyyyy looxi");
                                            try {
                                                throw e;
                                            } catch (Throwable ex) {
                                                throw new AppException(ErrorCode.UNAUTHENTICATED,"khong co quyen");
                                            }
                                        })
                                        .map(result -> result > 0).doOnSuccess(count -> log.info("Count for model: {}", count)); // Ghi lại số lượng
                            } else {
                                log.error("tryyyyyyyyyyyyyy 444");
                                // Trường hợp có cả model và action và raw
                                return permissionActionRawRepository.countActionModelRawAuthors(username, rawId, action, model)
                                        .map(result -> result > 0) .doOnSuccess(count -> log.info("Count for model: {}", count)); // Ghi lại số lượng
                            }
                        } catch (DataAccessException e) {
                            // Xử lý lỗi cơ sở dữ liệu
                            log.error("Database error while checking permissions: ", e);
                            return Mono.error(new RuntimeException("An error occurred while checking permissions.", e));
                        } catch (Exception e) {
                            // Xử lý lỗi chung
                            log.error("Unexpected error while checking permissions: ", e);
                            return Mono.error(new RuntimeException("An unexpected error occurred while checking permissions.", e));
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("Error in hasAuthor: ", e);
                        return Mono.just(false); // hoặc xử lý khác tùy vào yêu cầu
                    });
        }























}

//    SELECT 1
//    FROM permission_action_model_raw
//    WHERE model_id = (SELECT id FROM model WHERE name = :model)
//    AND action_id = (SELECT id FROM action WHERE name = :action)
//    AND raw_id = :rawId
//    AND permission_id IN (
//            SELECT permission_id
//      FROM users_permission
//              WHERE user_id = (SELECT id FROM users WHERE name = :username)
//  );




//    SELECT EXISTS (
//            SELECT 1
//            FROM permission_action_model_raw pamr
//            JOIN model m ON pamr.model_id = m.id
//            JOIN action a ON pamr.action_id = a.id
//            JOIN users_permission up ON pamr.permission_id = up.permission_id
//            JOIN users u ON up.user_id = u.id
//            WHERE m.name = :model
//            AND a.name = :action
//            AND pamr.raw_id = :rawId
//            AND u.name = :username
//    );
//







































//    SELECT 1
//    FROM permission_action_model_raw
//    WHERE model_id = (SELECT id FROM model WHERE name = :model)
//    AND action_id = (SELECT id FROM action WHERE name = :action)
//    AND raw_id = :rawId
//    AND permission_id IN (
//            SELECT permission_id
//      FROM users_permission
//              WHERE user_id = (SELECT id FROM users WHERE name = :username)
//  );




//    SELECT EXISTS (
//            SELECT 1
//            FROM permission_action_model_raw pamr
//            JOIN model m ON pamr.model_id = m.id
//            JOIN action a ON pamr.action_id = a.id
//            JOIN users_permission up ON pamr.permission_id = up.permission_id
//            JOIN users u ON up.user_id = u.id
//            WHERE m.name = :model
//            AND a.name = :action
//            AND pamr.raw_id = :rawId
//            AND u.name = :username
//    );
//





































