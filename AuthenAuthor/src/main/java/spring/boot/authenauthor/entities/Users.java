package spring.boot.authenauthor.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.services.impl.UserPermissionServiceImpl;


import java.util.*;
import java.util.stream.Collectors;


@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Users   extends BaseEntity  {


//    @Id
//    private String id;

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String name;

    private String username;

    private String phoneNumber;

    private String email;

    private String password;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Users() {
        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Phương thức này sẽ được cập nhật trong UserDetailsService
//        return Collections.emptyList();
//    }





//
//    @Override
//    public String getUsername() {
//        return username; // Changed from email to username
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}



//
//    @OneToMany
//    @JoinColumn(name = "users_id")
//    private Set<Posts> posts;
//
//    @OneToMany
//    @JoinColumn(name = "users_id")
//    private Set<Products> products;
//
//    @OneToMany
//    @JoinColumn(name = "users_id")
//    private Set<UserPermission> userPermissions;
//    // Other fields and methods...

//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        List<Integer> permissionIdList = usersPermissionRepository.findAllPermissionByUserId(id);
//        List<Permission> permissionList = permissionRepository.findByIdIn(permissionIdList);
//
//        // Lấy tất cả các quyền của người dùng từ UserPermissions
//        for (Permission userPermission : permissionList) {
//            authorities.add(new SimpleGrantedAuthority("PERMISSION_" + userPermission.getName().toUpperCase()));
//        }
//
//  