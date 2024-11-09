package spring.boot.authenauthor.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.Optional;
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends ReactiveCrudRepository<T, ID>
//        , ReactiveSortingRepository<T, ID>
{
    // Các phương thức tùy chỉnh có thể được định nghĩa ở đây
}