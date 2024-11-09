//package spring.boot.authenauthor.services.justService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//import spring.boot.authenauthor.entities.Permission;
//import spring.boot.authenauthor.repositories.PermissionRepository;
//import spring.boot.authenauthor.repositories.UsersPermissionRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class UserPermissionService {
//
//    @Autowired
//    private UsersPermissionRepository usersPermissionRepository;
//
//    @Autowired
//    private PermissionRepository permissionRepository;
//
//    public List<GrantedAuthority> getAuthoritiesForUser(Integer userId) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        List<Integer> permissionIdList = usersPermissionRepository.findAllPermissionByUserId(userId);
//        List<Permission> permissionList = permissionRepository.findByIdIn(permissionIdList);
//
//        for (Permission permission : permissionList) {
//            authorities.add(new SimpleGrantedAuthority("PERMISSION_" + permission.getName().toUpperCase()));
//        }
//
//        return authorities;
//    }
//}
