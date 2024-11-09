package spring.boot.authenauthor.services;

import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserPermissionService {

    Flux<GrantedAuthority> getAuthoritiesForUserById(String userId);
}
