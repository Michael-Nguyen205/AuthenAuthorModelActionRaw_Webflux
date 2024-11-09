package spring.boot.authenauthor.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.enums.entityRelationship.PermissionEntityRelationship;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.response.ApiResponse;
import spring.boot.authenauthor.models.response.ModelResponse;
import spring.boot.authenauthor.models.response.PermissionResponse;
import spring.boot.authenauthor.repositories.PermissionRepository;

import spring.boot.authenauthor.services.impl.PermissionServiceImpl;
import spring.boot.authenauthor.utils.EntityCascadeDeletionUtil;


import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/permission")
public class PermissionController {

    private final ModelMapper modelMapper;
    private final PermissionServiceImpl permissionService;
    private final PermissionRepository permissionRepository;
//    private final EntityCascadeDeletionUtil<Permission, Integer> deletionUtil;
    private final EntityCascadeDeletionUtil deletionUtil;





    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PermissionResponse> getPermission(@PathVariable Integer id) {
        log.error("Đã vào đây actions controller");
        return permissionRepository.findById(id)
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.DATA_NOT_FOUND, "khong thay perrmission")))
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(entity -> PermissionResponse.builder()
                        .name(entity.getName())
                        .id(entity.getId())
                        // Gán các trường khác từ model
                        .build());
    }








    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> deletePermission(@PathVariable Integer id) {
        log.error("đã vào trong deletePermission");

        // Truyền enum vào phương thức cascadeDeletion
        List<Enum> entityRelationships = Arrays.asList(PermissionEntityRelationship.values());

        return permissionService.deleteById(id)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
//                .then(deletionUtil.cascadeDeletion(entityRelationships, id,permissionRepository))
                .then(deletionUtil.deleteByParentId(id,"permission_id"))
                .then(Mono.just(new ApiResponse(null, "Permission deleted successfully", null, null)));
    }





    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@authorUtils.hasAuthor('VIEW','PERMISSION',null)")
    public Flux<PermissionResponse> getAllPermission() {  // Đổi kiểu trả về thành Flux<PermissionResponse>
        return permissionService.findAll()
                .doOnEach(modelsSignal -> log.error("đã vào trong findAll"))
                .doOnSubscribe(subscription -> log.error("đã vào trong findAll"))
                .onErrorResume(AppException.class, e -> {
                    log.error("li 1");

                    throw e;
                })
                .onErrorResume(BadSqlGrammarException.class, e -> {
                    log.error(" looxi 2");

                    throw e;
                })
                .map(permission -> PermissionResponse.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .build());
    }



//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Mono<ApiResponse> deletePermission(@PathVariable Integer id) {
//        log.error("đã vào trong deletePermission");
//        return permissionService.deleteById(id)
//                .onErrorResume(AppException.class, e -> {
//                    throw e;
//                })
//                .then( Mono.just(new ApiResponse(null,"Permisson delete successfully", null,null) ) );
//
//    }



//    @DeleteMapping("/{id}")
//    public Mono<ApiResponse> deletePermission(@PathVariable Integer id) {
//        return permissionRepository.findById(id)
//                .doOnNext(permission -> log.info("Permission ID: {}", permission.getId()))
//                .flatMap(permission -> permissionService.deleteById(id))
//                .map(deleted -> new ApiResponse(null, "Permission deleted successfully", null, null))
//                .onErrorResume(e -> {
//                    log.error("Error deleting permission with ID {}: {}", id, e.getMessage());
//                    return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Delete failed"));
//                });
//    }








//
//    @PostMapping
//    @ResponseStatus(HttpStatus.OK)
//    public Mono<ApiResponse> createPermisson(@RequestBody @Valid CreateModelRequest request) {
//
//        // Thiết lập thông tin đơn hàng
//        if (modelMapper.getTypeMap(CreateModelRequest.class, Models.class) == null) {
//            modelMapper.typeMap(CreateModelRequest.class, Models.class);
//        }
//
//        Models models = new Models();
//        modelMapper.map(request, models);
//
////        models.setName(request.getName());
////        models.setDescription(request.getDescription());
//        return modelService.save(models)
//                .onErrorResume(AppException.class, e -> {
//                    throw e;
//                })
//                .map(model -> new ApiResponse(null,"Model created successfully", null,model));
//    }



//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Mono<ApiResponse> getModelById(@PathVariable Integer id) {
//        return modelService.getModelById(id)
//                .map(model -> new ApiResponse.builder()
//                .switchIfEmpty(Mono.just(new ApiResponse("Model not found", null)));
//    }
//
//    // UPDATE
//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Mono<ApiResponse> updateModel(@PathVariable Integer id, @RequestBody @Valid CreateModelRequest request) {
//        return modelService.updateModel(id, request)
//                .map(model -> new ApiResponse("Model updated successfully", model))
//                .switchIfEmpty(Mono.just(new ApiResponse("Model not found", null)));
//    }
//
//    // DELETE
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Mono<ApiResponse> deleteModel(@PathVariable Integer id) {
//        return modelService.deleteModel(id)
//                .then(Mono.just(new ApiResponse("Model deleted successfully", null)))
//                .switchIfEmpty(Mono.just(new ApiResponse("Model not found", null)));
//    }


}
