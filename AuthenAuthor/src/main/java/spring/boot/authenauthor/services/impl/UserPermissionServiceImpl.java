package spring.boot.authenauthor.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.repositories.PermissionRepository;
import spring.boot.authenauthor.repositories.UsersPermissionRepository;
import spring.boot.authenauthor.services.IUserPermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserPermissionServiceImpl implements IUserPermissionService {

    @Autowired
    private UsersPermissionRepository usersPermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    @Override
    public Flux<GrantedAuthority> getAuthoritiesForUserById(String userId) {
        // Lấy danh sách permissionId
        return usersPermissionRepository.findAllPermissionByUserId(userId)
                .collectList() // Chuyển đổi từ Flux sang List<Long>
                .flatMapMany(permissionIdList ->
                        // Chuyển đổi List<Long> thành List<Integer>
                        permissionRepository.findByIdIn(permissionIdList.stream()
                                        .map(Long::intValue) // Chuyển đổi Long thành Integer
                                        .collect(Collectors.toList())) // Thu thập thành List<Integer>
                                .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission.getName().toUpperCase())) // Chuyển đổi thành GrantedAuthority
                );
    }
}




