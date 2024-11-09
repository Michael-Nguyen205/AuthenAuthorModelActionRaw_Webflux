package spring.boot.authenauthor.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.repositories.UserRepository;
import spring.boot.authenauthor.services.impl.UserPermissionServiceImpl;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Autowired
    private UserPermissionServiceImpl userPermissionService;

    @Autowired
    private UserRepository usersRepository;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return usersRepository.findByEmail(email)
                .flatMap(user ->
                        userPermissionService.getAuthoritiesForUserById(user.getId())
                                .collectList() // Chuyển đổi Flux<GrantedAuthority> thành Mono<List<GrantedAuthority>>
                                .map(authorities -> (UserDetails) new org.springframework.security.core.userdetails.User(
                                        user.getUsername(),
                                        user.getPassword(),
                                        authorities // Sử dụng danh sách quyền đã thu thập
                                ))
                )
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }
}