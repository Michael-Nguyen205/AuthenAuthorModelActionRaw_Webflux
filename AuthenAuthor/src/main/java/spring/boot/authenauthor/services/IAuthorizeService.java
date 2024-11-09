package spring.boot.authenauthor.services;


import reactor.core.publisher.Mono;
import spring.boot.authenauthor.models.requests.AddAuthorForUserRequest;
import spring.boot.authenauthor.models.requests.CreatePermissionRequest;
import spring.boot.authenauthor.models.requests.RemoveAuthorForUserRequest;
import spring.boot.authenauthor.models.response.ApiResponse;
import spring.boot.authenauthor.models.response.AuthorizeResponse;
import spring.boot.authenauthor.models.response.UserPermissionResponse;

public interface IAuthorizeService {

    Mono<AuthorizeResponse> createAuthor(CreatePermissionRequest permissionRequest);

    Mono<UserPermissionResponse> addAuthor(AddAuthorForUserRequest permissionRequest);

    Mono<AuthorizeResponse> updateAuthor(CreatePermissionRequest permissionRequest ,Integer Id);

    Mono<UserPermissionResponse> removeAuthorForUser(RemoveAuthorForUserRequest permissionRequest);

}
