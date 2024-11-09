package spring.boot.authenauthor.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Action;
import spring.boot.authenauthor.entities.Models;
import spring.boot.authenauthor.enums.entityRelationship.PermissionEntityRelationship;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.requests.CreateActionRequest;
import spring.boot.authenauthor.models.requests.CreateModelRequest;
import spring.boot.authenauthor.models.response.ActionResponse;
import spring.boot.authenauthor.models.response.ApiResponse;
import spring.boot.authenauthor.models.response.ModelResponse;
import spring.boot.authenauthor.repositories.ActionsRepository;
import spring.boot.authenauthor.services.IModelService;
import spring.boot.authenauthor.services.impl.ActionsServiceImpl;
import spring.boot.authenauthor.services.impl.ModelsServiceImpl;
import spring.boot.authenauthor.utils.EntityCascadeDeletionUtil;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/actions")
public class ActionController {

    private final ActionsServiceImpl actionsService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final ActionsRepository actionsRepository;




    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ActionResponse> getActions( @PathVariable Integer id) {
        log.error("Đã vào đây actions controller");
        return actionsRepository.findById(id)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(action -> ActionResponse.builder()
                        .name(action.getName()) // Gán id từ model
                         // Gán các trường khác từ model
                        .build());
    }




    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ActionResponse> getAllActions() {
        log.error("Đã vào đây actions controller");
        return actionsService.findAll()
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(action -> ActionResponse.builder()
                        .name(action.getName()) // Gán id từ model
                        // Gán các trường khác từ model
                        .build());
    }



    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> createAction(@RequestBody @Valid CreateActionRequest request) {
        Action action = new Action();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        return actionsService.save(action)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(model -> new ApiResponse(null,"Action created successfully", null,model));
    }




    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> updateAction(@RequestBody @Valid CreateActionRequest request, @PathVariable Integer id) {
        return actionsRepository.findById(id)
                .flatMap(existingEntity -> {
                    existingEntity.setName(request.getName());
                    existingEntity.setDescription(request.getDescription());

                    return actionsService.update(existingEntity)
                            .map(updatedEntity -> new ApiResponse(null, "Action updated successfully", null, updatedEntity));
                })
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.DATA_NOT_FOUND,"khong tim thay action")))
                .onErrorResume(AppException.class, e -> {
                    throw e;
                });
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> deleteAction(@PathVariable Integer id) {
        log.error("đã vào trong deleteactions");
        // Truyền enum vào phương thức cascadeDeletion
        List<Enum> entityRelationships = Arrays.asList(PermissionEntityRelationship.values());
        return actionsService.deleteById(id)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
//                .then(deletionUtil.cascadeDeletion(entityRelationships, id,permissionRepository))
                .then(deletionUtil.deleteByParentId(id,"action_id"))
                .then(Mono.just(new ApiResponse(null, "actions deleted successfully", null, null)));
    }






    // READ - Lấy tất cả các mô hình

    // READ - Lấy một mô hình theo ID



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
