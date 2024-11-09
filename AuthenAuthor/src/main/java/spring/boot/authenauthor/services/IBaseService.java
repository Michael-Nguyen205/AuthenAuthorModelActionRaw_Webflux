package spring.boot.authenauthor.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public interface IBaseService<T, ID extends Serializable> {

    // Trả về tất cả các entity dưới dạng Flux<T>
    Flux<T> findAll();

    // Trả về một entity theo ID dưới dạng Mono<T>
    Mono<T> findById(ID id);

    // Lưu một entity mới dưới dạng Mono<T>
    Mono<T> save(T entity);

    // Cập nhật entity theo ID dưới dạng Mono<T>
    Mono<T> update( T entity);

    // Xóa một entity theo ID và trả về Mono<Void>
    Mono<Void> deleteById(ID id);
}
