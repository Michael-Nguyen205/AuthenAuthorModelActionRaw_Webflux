package spring.boot.authenauthor.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.entities.PermissionActionModelRaw;
import spring.boot.authenauthor.entities.UserPermission;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.requests.*;
import spring.boot.authenauthor.models.response.*;
import spring.boot.authenauthor.repositories.*;
import spring.boot.authenauthor.services.IAuthorizeService;
import spring.boot.authenauthor.utils.EntityCascadeDeletionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthorizeServiceImpl implements IAuthorizeService {

    private final PermissionRepository permissionRepository;
    //    private final Author permissionRepository;
    private final ActionsRepository actionsRepository;
    private final PermissionActionModelRawRepository permissionActionModelRawRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final UserRepository userRepository;
    private final UsersPermissionRepository usersPermissionRepository;
    private final DatabaseClient databaseClient;



    @Override
    public Mono<UserPermissionResponse> addAuthor(AddAuthorForUserRequest permissionRequest) {
        String emailUser = permissionRequest.getUserEmail();
        Integer permissionId = permissionRequest.getPermissionId();
        return userRepository.findByEmail(emailUser)
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy user")))
                .flatMap(user ->
                        usersPermissionRepository.findByUserIdAndPermissionId(user.getId(), permissionId)
                                .flatMap(existingUserPermission -> {
                                    // Trả về thông báo khi quyền đã tồn tại
                                    return Mono.just(UserPermissionResponse.builder()
                                            .userId(user.getId())
                                            .permissionId(permissionId)
                                            .build()
                                    );
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    // Tạo mới UserPermission nếu không tìm thấy
                                    UserPermission newUserPermission = new UserPermission();
                                    newUserPermission.setId(UUID.randomUUID().toString());
                                    newUserPermission.setUserId(user.getId());
                                    newUserPermission.setPermissionId(permissionId);
                                    return usersPermissionRepository.save(newUserPermission)
                                            .map(savedUserPermission -> UserPermissionResponse.builder()
                                                    .userId(user.getId())
                                                    .permissionId(permissionId)
                                                    .build()
                                            );
                                }))
                );
    }

    @Override
    public Mono<UserPermissionResponse> removeAuthorForUser(RemoveAuthorForUserRequest permissionRequest) {
        String emailUser = permissionRequest.getUserEmail();
        Integer permissionId = permissionRequest.getPermissionId();
        return userRepository.findByEmail(emailUser)
                .switchIfEmpty(Mono.error(new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy user")))
                .flatMap(user ->
                        usersPermissionRepository.findByUserIdAndPermissionId(user.getId(), permissionId)
                                .flatMap(existingUserPermission -> {
                                    log.error("đã vào existingUserPermission");
                                    // Trả về thông báo khi quyền đã tồn tại
                                    return usersPermissionRepository.deleteUserPermissionByUserIdAndPermissionId(existingUserPermission.getUserId(), existingUserPermission.getPermissionId())
                                            .onErrorResume(ClassCastException.class, e -> {
                                                log.error("loiiiiiii");
                                                throw new AppException(ErrorCode.DATABASE_DELETE_ERROR, "loi xoa du lieu");
                                            })
                                            .map(deletedUserPermission -> UserPermissionResponse.builder()
                                                    .userId(user.getId())
                                                    .permissionId(permissionId)
                                                    .build()
                                            ).onErrorResume(e -> {
                                                log.error("oiiiiiiiiiiii");
                                                e.printStackTrace();
                                                try {
                                                    throw e;
                                                } catch (Throwable ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            });
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    // Tạo mới UserPermission nếu không tìm thấy
                                    throw new AppException(ErrorCode.DATA_NOT_FOUND, "khong tim thay de xoa");
                                }))
                );
    }


    @Override
    public Mono<AuthorizeResponse> createAuthor(CreatePermissionRequest permissionRequest) {
        log.error("đã vao trong createAuthor");
        log.error("permissionid: {}", permissionRequest.getId());

        Integer permissionId = permissionRequest.getId() == null ? 0 : permissionRequest.getId();
        // Tìm Permission theo ID
        // Chuyển đổi ngoại lệ thành AppException
        return createOrUpdatePermission(permissionRequest,permissionId)
                .flatMap(savedPermission -> {

                    List<PermissionActionModelRaw> permissionActionModelRawList = new ArrayList<>();
                    List<ModelResponse> modelResponseList = processModelRequest(permissionActionModelRawList,permissionId,permissionRequest)  ;
                    log.error("modelResponseList:{}",modelResponseList);
                    // Tạo phản hồi Permission
                    AuthorizeResponse authorizeResponse = createAuthorizeResponse(savedPermission, modelResponseList);

                    // Lưu tất cả PermissionActionModelRaw

                    permissionActionModelRawList.forEach(permissionModelActionRaw -> {
                        if (permissionModelActionRaw.getId() == null) {
                            throw new IllegalStateException("ID must not be null before saving");
                        }
                    });

                    // Chuyển đổi ngoại lệ thành AppException
                    return generateInsertQuery(permissionActionModelRawList)
                            .then(Mono.just(authorizeResponse))
                            .onErrorResume(IllegalArgumentException.class, e -> {
                                log.error("đã vao IllegalArgumentException");
                                throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi cập nhật permissionActionRaw vào cơ sở dữ liệu: " + e.getMessage());
                            })
                            .onErrorResume(Mono::error);
                })
                .onErrorResume(Mono::error)
                .doOnSubscribe(subscription -> log.error("Luồng đã được subscribe tại findById"));
    }


    @Override
    public Mono<AuthorizeResponse> updateAuthor(CreatePermissionRequest permissionRequest, Integer permissionId) {
        // Kiểm tra permissionId
        if (permissionId == null) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }

        // Tìm Permission theo ID và xử lý ngoại lệ
        return createOrUpdatePermission(permissionRequest, permissionId)
                .flatMap(savedPermission -> {

                    log.error("Đã lưu Permission: {}", savedPermission);

                    // Xóa Permission Actions cũ trước khi cập nhật
                    return clearPermissionActionsForUpdate(permissionId)
                            .then(Mono.defer(() -> {
                                List<PermissionActionModelRaw> permissionActionModelRawList = new ArrayList<>();
                                List<ModelResponse> modelResponseList = processModelRequest(permissionActionModelRawList,permissionId,permissionRequest)  ;
                                log.error("modelResponseList:{}",modelResponseList);
                                // Tạo phản hồi Permission
                                AuthorizeResponse authorizeResponse = createAuthorizeResponse(savedPermission, modelResponseList);

                                // Lưu tất cả PermissionActionModelRaw vào cơ sở dữ liệu và trả về phản hồi
                                return saveAllBatch(permissionActionModelRawList, 3)
                                        .then(Mono.just(authorizeResponse))
                                        .onErrorResume(IllegalArgumentException.class, e -> {
                                            log.error("Lỗi khi cập nhật permissionActionRaw vào cơ sở dữ liệu: {}", e.getMessage());
                                            throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi cập nhật permissionActionRaw vào cơ sở dữ liệu: " + e.getMessage());
                                        });
                            }));
                })
                .doOnSubscribe(subscription -> log.error("Đang subscribe tại findById"))
                .onErrorResume(Mono::error);  // Đảm bảo có giá trị trả về cho mọi nhánh
    }


    private Mono<Permission> createOrUpdatePermission(CreatePermissionRequest permissionRequest, Integer permissionId) {

        return permissionRepository.findById(permissionId)
                .onErrorResume(e -> {
                    log.error("đã vao IllegalArgumentException");
                    throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi permissionRepository.");
                })
                .flatMap(existingPermission -> {
                    log.error("đã vao trong existingPermission");
                    // Nếu đã tồn tại, cập nhật thông tin
                    existingPermission.setName(permissionRequest.getName());
                    return permissionRepository.save(existingPermission);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("đã vao trong switchIfEmpty");
                    // Nếu không tồn tại, tạo mới Permission
                    Permission newPermission = new Permission();
                    log.error("permissionRequest.getName: {}", permissionRequest.getName());
                    newPermission.setName(permissionRequest.getName());
                    return permissionRepository.save(newPermission)
                            .onErrorResume(DataIntegrityViolationException.class, e -> {
                                log.error("đã vao DataIntegrityViolationException");
                                e.printStackTrace();
                                throw new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi lưu permission");
                            });
                }));
    }


    private Mono<Void> clearPermissionActionsForUpdate(Integer permissionId) {
        return permissionActionModelRawRepository.findByPermissionId(permissionId)
                .onErrorResume(e -> {
                    try {
                        throw e;
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .hasElements()
                .flatMap(hasElements -> {
                    log.error("da vao permissionIdCount");
                    if (hasElements) {
                        return deletionUtil.deleteByEntityParentIdOnTable(permissionId, "permission_model_action_raw")
                                .then();

                    } else {
                        // Xóa dữ liệu và lưu lại Permission
                        return Mono.empty();

                    }
                });
    }


    private List<ModelResponse> processModelRequest( List<PermissionActionModelRaw> permissionActionModelRawList, Integer permissionId ,CreatePermissionRequest permissionRequest) {
        List<ModelResponse> modelResponseList = new ArrayList<>();
        // Duyệt qua danh sách modelRequestList và xử lý từng mẫu
        for (CreateModelRequest modelRequest : permissionRequest.getModelRequestList()) {
            ModelResponse modelResponse =new ModelResponse();
            modelResponse.setModelId(modelRequest.getId());
            List<ActionResponse> actionResponseList = processActionRequests(modelRequest.getActionRequestList(), permissionActionModelRawList, permissionId, modelRequest.getId());
            modelResponse.setActionResponses(actionResponseList);
            modelResponseList.add(modelResponse);
        }
        return modelResponseList;
    }


    private List<ActionResponse> processActionRequests(List<CreateActionRequest> actionRequestList, List<PermissionActionModelRaw> permissionActionModelRawList, Integer permissionId, Integer modelId) {
        List<ActionResponse> actionResponseList = new ArrayList<>();
        for (CreateActionRequest actionRequest : actionRequestList) {
            ActionResponse actionResponse = new ActionResponse();
            actionResponse.setActionId(actionRequest.getActionId());
            List<RawResponse> rawResponseList = processRawRequests(actionRequest.getRawRequests(), permissionActionModelRawList, permissionId, modelId, actionRequest.getActionId());
            actionResponse.setRawResponse(rawResponseList);
            actionResponseList.add(actionResponse);
        }
        return actionResponseList;
    }

    private List<RawResponse> processRawRequests(List<CreateRawRequest> rawRequestList, List<PermissionActionModelRaw> permissionActionModelRawList, Integer permissionId, Integer modelId, Integer actionId) {
        List<RawResponse> rawResponseList = new ArrayList<>();
        if (rawRequestList == null || rawRequestList.isEmpty()) {
            // Tạo PermissionActionModelRaw với null rawId
            permissionActionModelRawList.add(createPermissionActionModelRaw(permissionId, modelId, actionId, null));
        } else {
            for (CreateRawRequest rawRequest : rawRequestList) {
                RawResponse rawResponse = new RawResponse();
                rawResponse.setRawId(rawRequest.getRawId());
                rawResponseList.add(rawResponse);

                // Tạo PermissionActionModelRaw với rawId thực tế
                permissionActionModelRawList.add(createPermissionActionModelRaw(permissionId, modelId, actionId, rawRequest.getRawId()));
            }
        }
        return rawResponseList;
    }


    private PermissionActionModelRaw createPermissionActionModelRaw(Integer permissionId, Integer modelId, Integer actionId, Integer rawId) {

        PermissionActionModelRaw permissionActionModelRaw = new PermissionActionModelRaw();
        permissionActionModelRaw.setId(UUID.randomUUID().toString());
        permissionActionModelRaw.setPermissionId(permissionId);
        permissionActionModelRaw.setActionId(actionId);
        permissionActionModelRaw.setModelId(modelId);
        if (rawId == null) {
            permissionActionModelRaw.setRawId(rawId);
        }
        permissionActionModelRaw.setRawId(rawId);
        return permissionActionModelRaw;
    }


    private AuthorizeResponse createAuthorizeResponse(Permission savedPermission, List<ModelResponse> modelResponseList) {
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setId(savedPermission.getId());
        permissionResponse.setName(savedPermission.getName());
        permissionResponse.setModelResponses(modelResponseList);
        authorizeResponse.setPermissonResponse(permissionResponse);

        return authorizeResponse;
    }


    public Mono<Void> saveAllBatch(List<PermissionActionModelRaw> permissionActionModelRawList, int batchSize) {
        return Flux.fromIterable(permissionActionModelRawList) // 1. Chuyển đổi danh sách thành Flux
                .buffer(batchSize) // 2. Chia danh sách thành các nhóm với kích thước bằng batchSize
                .concatMap(batch -> // 3. Xử lý từng batch một theo thứ tự
                        Flux.fromIterable(batch) // 4. Chuyển đổi batch thành Flux
                                .flatMap(permissionActionModelRaw -> // 5. Đối với từng phần tử trong batch, thực hiện insert vào cơ sở dữ liệu
                                        r2dbcEntityTemplate.insert(PermissionActionModelRaw.class)
                                                .using(permissionActionModelRaw) // 6. Chèn đối tượng vào cơ sở dữ liệu
                                )
                )
                .then(); // 7. Sau khi tất cả các batch được xử lý, trả về Mono<Void> khi hoàn thành
    }






    public Mono<Void> generateInsertQuery(List<PermissionActionModelRaw> list) {
        // Tạo phần đầu câu lệnh SQL
        StringBuilder query = new StringBuilder("INSERT INTO permission_model_action_raw (id, permission_id, action_id, model_id, raw_id) VALUES ");

        // Duyệt qua danh sách và tạo các chuỗi giá trị
        String values = list.stream()
                .map(item -> String.format("('%s', %d, %d, %d, %d)",
                        item.getId(),
                        item.getPermissionId(),
                        item.getActionId(),
                        item.getModelId(),
                        item.getRawId()))
                .collect(Collectors.joining(", "));  // Ghép các giá trị lại với nhau

        // Thêm các giá trị vào câu lệnh SQL
        query.append(values);

        // Thực thi câu truy vấn SQL và trả về số dòng bị ảnh hưởng
        return databaseClient.sql(query.toString())
                .fetch()
                .rowsUpdated()
                .flatMap(rowsAffected -> {
                    if (rowsAffected > 0) {
                        log.info("Rows affected: {}", rowsAffected);
                        return Mono.empty(); // Trả về Mono.empty() nếu insert thành công
                    } else {
                        return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Không có bản ghi nào được chèn"));
                    }
                })
                .then()
                .onErrorResume(e -> {
                    e.printStackTrace();
                    log.error("Lỗi khi thực thi truy vấn SQL: {}", e.getMessage());
                    return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi lưu PermissionActionModelRaw"));
                });
    }


}
