package spring.boot.authenauthor.repositories;

//import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.entities.Users;

import java.util.Optional;


public interface UserRepository extends BaseRepository<Users, Integer> {
//    boolean existsByEmail(String email);


    //    Optional<Users> findByPhoneNumber(String phoneNumber);
//    Optional<Users> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);


    Mono<Users> findByUsername(String username);

    Mono<Users> findByEmail(String email);
    Mono<Users> findByPhoneNumber(String phoneNumber);
}