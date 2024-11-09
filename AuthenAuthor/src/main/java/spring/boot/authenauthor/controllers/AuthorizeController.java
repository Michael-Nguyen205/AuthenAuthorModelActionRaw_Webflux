package spring.boot.authenauthor.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.requests.AddAuthorForUserRequest;
import spring.boot.authenauthor.models.requests.CreatePermissionRequest;
import spring.boot.authenauthor.models.requests.RemoveAuthorForUserRequest;
import spring.boot.authenauthor.models.response.ApiResponse;
import spring.boot.authenauthor.models.response.AuthorizeResponse;
import spring.boot.authenauthor.models.response.UserPermissionResponse;
import spring.boot.authenauthor.repositories.PermissionRepository;
import spring.boot.authenauthor.services.IAuthorizeService;

@Log4j2
@RestController
@RequestMapping("${api.prefix}/authorize")
@RequiredArgsConstructor
public class AuthorizeController {

    private final IAuthorizeService permissionService;
    private final PermissionRepository permissionRepository;


//    @PostMapping ("")
//    public Mono<ServerResponse> createPermission(@Valid ServerRequest request) {
//        return request.bodyToMono(CreatePermissionRequest.class)
//                .flatMap(createPermissionRequest ->
//                        permissionService.createPermission(createPermissionRequest)
//                                .flatMap(permissionResponse ->
//                                        ServerResponse.ok()
//                                                .contentType(MediaType.APPLICATION_JSON)
//                                                .bodyValue(permissionResponse)
//                                )
//                );
//    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthorizeResponse> updateAuthor(@RequestBody @Valid CreatePermissionRequest request,@PathVariable Integer id) {
        log.error("da vao updateAuthor");
        return permissionRepository.findById(id)
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.DATA_NOT_FOUND, "khong thay perrmission")))
                .then(permissionService.updateAuthor(request, id))
                .onErrorResume(AppException.class, e -> {
                    throw e;
                });
    }


    @PostMapping ("")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthorizeResponse> createAuthor(@RequestBody @Valid CreatePermissionRequest request) {
        log.error("da vao day");
        return permissionService.createAuthor(request)
//                .onErrorResume(IllegalArgumentException.class,e -> {
//                    log.error("đã vao IllegalArgumentException");
//                    throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi permissionRepository.findById" );
//                })
                .onErrorResume(AppException.class, e -> {
                    throw e;
                });
    }




    @PostMapping ("/addAuthorForUser")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserPermissionResponse> addAuthorForUser(@RequestBody @Valid AddAuthorForUserRequest request) {
        log.error("da vao day");
        return permissionService.addAuthor(request)
//                .onErrorResume(IllegalArgumentException.class,e -> {
//                    log.error("đã vao IllegalArgumentException");
//                    throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi permissionRepository.findById" );
//                })
                .onErrorResume(AppException.class, e -> {
                    throw e;
                });
    }




    @DeleteMapping  ("/removeAuthorForUser")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> removeAuthorForUser(@RequestBody @Valid RemoveAuthorForUserRequest request) {
        log.error("da vao day");
        return permissionService.removeAuthorForUser(request)
                .map(userPermissionResponse->{
                    return
                            new ApiResponse(null,"removeAuthorForUser successfully", null,userPermissionResponse);
                })
//                .onErrorResume(IllegalArgumentException.class,e -> {
//                    log.error("đã vao IllegalArgumentException");
//                    throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi permissionRepository.findById" );
//                })
                .onErrorResume(AppException.class, e -> {
                    throw e;
                });
    }




}

