//package spring.boot.authenauthor.services.justService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Service;
//import spring.boot.authenauthor.entities.Permission;
//import spring.boot.authenauthor.entities.Users;
//import spring.boot.authenauthor.repositories.PermissionActionRawRepository;
//import spring.boot.authenauthor.repositories.PermissionRepository;
//import spring.boot.authenauthor.repositories.UsersPermissionRepository;
//import java.util.Collection;
//import java.util.List;
//
//@Service
//public class AuthorizationService {
//    @Autowired
//    private PermissionRepository permissionRepository;
//    private PermissionActionRawRepository permissionActionRawRepository;
//    private UsersPermissionRepository usersPermissionRepository;
//    public boolean hasPermission(Authentication authentication, String action) {
//        Users user = (Users) authentication.getPrincipal();
//
//        // Lấy danh sách PermissionId của user từ UsersPermission
//        List<Integer> permissionIdList = usersPermissionRepository.findAllPermissionByUserId(user.getId());
//
//        List<Permission> permissionList = permissionRepository.findByIdIn(permissionIdList);
//
//
//        // Lấy danh sách Authorities từ authentication (được định dạng như "PERMISSION_xxx")
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // Duyệt qua danh sách PermissionId của người dùng
//        for (Permission permission : permissionList) {
//            // Tạo tên quyền từ PermissionId để so sánh với Authorities
//            String permissionName = "PERMISSION_" + permission.getName();
//
//
//            // Kiểm tra xem authorities có chứa permissionName này không
//            boolean hasPermission = authorities.stream()
//                    .anyMatch(authority -> authority.getAuthority().equals(permissionName));
//
//            if (hasPermission) {
//                // Nếu có quyền, kiểm tra danh sách actionIds
//                List<Integer> actionIds = permissionActionRawRepository.findActionIdsByPermissionId(permission.getId());
//
//                // Kiểm tra xem action đầu vào có nằm trong danh sách actionIds không
//                if (actionIds.contains(Integer.parseInt(action))) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//
//}
