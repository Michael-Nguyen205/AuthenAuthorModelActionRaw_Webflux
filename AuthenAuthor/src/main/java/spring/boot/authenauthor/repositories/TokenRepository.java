package spring.boot.authenauthor.repositories;




import reactor.core.publisher.Flux;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.entities.Tokens;
import spring.boot.authenauthor.entities.Users;

import java.util.List;

public interface TokenRepository extends BaseRepository<Tokens, Integer> {

   Flux<Tokens> findByUserId(String userId);
//    List<Tokens> findByUser(Users user);
    Tokens findByToken(String token);
    Tokens findByRefreshToken(String token);
}

