package spring.boot.authenauthor.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Models;
import spring.boot.authenauthor.enums.entityRelationship.PermissionEntityRelationship;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.requests.CreateActionRequest;
import spring.boot.authenauthor.models.requests.CreateModelRequest;
import spring.boot.authenauthor.models.response.ActionResponse;
import spring.boot.authenauthor.models.response.ApiResponse;
import spring.boot.authenauthor.models.response.ModelResponse;
import spring.boot.authenauthor.repositories.ModelsRepository;
import spring.boot.authenauthor.services.IModelService;
import spring.boot.authenauthor.services.impl.ModelsServiceImpl;
import spring.boot.authenauthor.utils.EntityCascadeDeletionUtil;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/models")
public class ModelController {


    private final ModelMapper modelMapper;
    private final ModelsServiceImpl modelService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final ModelsRepository modelsRepository;




    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ModelResponse> getModel(@PathVariable Integer id) {
        log.error("Đã vào đây actions controller");
        return modelsRepository.findById(id)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(entity -> ModelResponse.builder()
                        .modelName(entity.getName())
                        .modelId(entity.getId())
                        .modelDescription(entity.getDescription())
                        // Gán các trường khác từ model
                        .build());
    }







    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ModelResponse> getAllModels() {
        log.error("Đã vào đây models controller");
        return modelService.findAll().doOnEach(modelsSignal -> {
                    log.error("đã vào trong findAll");
                })
                .doOnSubscribe(subscription -> log.error("đã vào trong findAll"))
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(model -> ModelResponse.builder()
                        .modelId(model.getId()) // Gán id từ model
                        .modelName(model.getName()) // Gán các trường khác từ model
                        .build());
    }



    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> createModel(@RequestBody @Valid CreateModelRequest request) {

        // Thiết lập thông tin đơn hàng
        if (modelMapper.getTypeMap(CreateModelRequest.class, Models.class) == null) {
            modelMapper.typeMap(CreateModelRequest.class, Models.class);
        }

        Models models = new Models();
        modelMapper.map(request, models);

//        models.setName(request.getName());
//        models.setDescription(request.getDescription());
        return modelService.save(models)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
                .map(model -> new ApiResponse(null,"Model created successfully", null,model));
    }





    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> deleteModel(@PathVariable Integer id) {
        log.error("đã vào trong deletemodels");
        // Truyền enum vào phương thức cascadeDeletion
        List<Enum> entityRelationships = Arrays.asList(PermissionEntityRelationship.values());

        return modelService.deleteById(id)
                .onErrorResume(AppException.class, e -> {
                    throw e;
                })
//                .then(deletionUtil.cascadeDeletion(entityRelationships, id,permissionRepository))
                .then(deletionUtil.deleteByParentId(id,"model_id"))
                .then(Mono.just(new ApiResponse(null, "models deleted successfully", null, null)));
    }



    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ApiResponse> updateModel(@RequestBody @Valid CreateModelRequest request, @PathVariable Integer id) {
        return modelsRepository.findById(id)
                .flatMap(existingEntity -> {
                    existingEntity.setName(request.getName());
                    existingEntity.setDescription(request.getDescription());
                    return modelService.update(existingEntity)
                            .map(updatedEntity -> new ApiResponse(null, "Model updated successfully", null, updatedEntity));
                })
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.DATA_NOT_FOUND,"khong tim thay model")))
                .onErrorResume(AppException.class, e -> {
                    throw e;
                });
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
