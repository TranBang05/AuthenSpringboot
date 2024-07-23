package com.example.demo_mhdigital.service.impl;

import com.example.demo_mhdigital.config.exception.ApiException;
import com.example.demo_mhdigital.data.request.RoleRequest;
import com.example.demo_mhdigital.data.mappers.PermissionMapper;
import com.example.demo_mhdigital.data.mappers.RoleMapper;
import com.example.demo_mhdigital.data.response.MessageResponses;
import com.example.demo_mhdigital.data.response.user.PermissionResponse;
import com.example.demo_mhdigital.data.response.user.RoleResponse;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import com.example.demo_mhdigital.data.user.UserAuthorDTO;
import com.example.demo_mhdigital.repositories.PermissionRespository;
import com.example.demo_mhdigital.repositories.RolePermisionRepository;
import com.example.demo_mhdigital.repositories.RoleRespository;
import com.example.demo_mhdigital.service.RoleService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.Role;
import org.com.app.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.demo_mhdigital.config.utils.SecurityUtils.extractUser;
import static com.example.demo_mhdigital.data.constant.ActionConstant.*;
import static com.example.demo_mhdigital.data.constant.ActionConstant.VIEW;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper mapper;

    @Autowired
    PermissionMapper mappers;

    @Autowired
    RoleRespository roleRespository;

    @Autowired
    PermissionRespository permissionRespository;

    @Autowired
    RolePermisionRepository rolePermisionRepository;

    protected String getPermissionCode() {
        return "role";
    }
    @Override
    public Single<String> addRole(RoleRequest roleRequest) {
        Role role = mapper.toPojo(roleRequest);

        return roleRespository.addRole(role)
                .map(roleInsert -> {
                    if (roleInsert.isPresent()) {
                        return "success";
                    } else {
                        throw new RuntimeException("error");
                    }
                }).onErrorReturnItem("error");
    }

   /*@Override
    public Single<String> updateRole(Integer id, RoleRequest roleRequest) {
        Role role = mapper.toPojo(roleRequest);
        return roleRespository.findbyids(id)
                .flatMap(users -> {
                    if (users.isPresent()) {
                        return roleRespository.update(id, role)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                });
    }*/

    @Override
    public Single<String> updateRole(Integer id, RoleRequest roleRequest) {
        Role role = mapper.toPojo(roleRequest);
        return roleRespository.findbyids(id)
                .flatMap(users -> {
                    if (users.isPresent()) {
                        return permissionRespository.updatePermissionId(id, roleRequest.getPermissionIds())
                                .flatMap(permissionUpdate -> {
                                    if (permissionUpdate != null) {
                                        return roleRespository.update(id, role)
                                                .map(x -> "success")
                                                .onErrorReturnItem("error");
                                    } else {
                                        return Single.just("err update permission ID");
                                    }
                                });
                    } else {
                        return Single.just("ID does not exist");
                    }
                });
    }

    @Override
    public Single<String> updatePermissionByRole(Integer id, RoleRequest roleRequest, Authentication authentication) {
        return  checkPermissionUpdate(authentication)
                .flatMap(actions -> {
                    Role role = mapper.toPojo(roleRequest);
                    return roleRespository.findbyids(id)
                            .flatMap(roles -> {
                                if (roles.isPresent()) {
                                    return roleRespository.findbyid(roleRequest.getPermissionIds())
                                            .flatMap(per -> {
                                                if (per == null || per.isEmpty()) {
                                                    return Single.just("Permission ID does not exist");
                                                } else {
                                                    return permissionRespository.updatePermissionId(id, roleRequest.getPermissionIds())
                                                            .flatMap(permissionUpdate -> {
                                                                if (permissionUpdate != null) {
                                                                    return roleRespository.update(id, role)
                                                                            .map(x -> "success")
                                                                            .onErrorReturnItem("error");
                                                                } else {
                                                                    return Single.just("Failed to update Permission ID");
                                                                }
                                                            });
                                                }
                                            });
                                } else {
                                    return Single.just("RoleID does not exist");
                                }
                            });
                });
    }

    @Override
    public Single<String> deleteRole(Integer id) {
        return roleRespository.findbyids(id)
                .flatMap(users -> {
                    if (users.isPresent()) {
                        return roleRespository.delete(id)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                });
    }

    @Override
    public @NonNull Single<Optional<?>> getById(Integer id) {
        return roleRespository.findbyids(id)
                .flatMap(roleOptional -> {
                    RoleResponse response = mapper.toResponse(roleOptional.orElse(null));
                    if (response != null) {
                        return permissionRespository.getPermissionByRoleId(id)
                                .flatMap(permissionList -> {
                                    List<PermissionResponse> permissionResponses = mappers.toResponses(permissionList);
                                    response.setPermisionResponses(permissionResponses);
                                    return Single.just(Optional.of(response));
                                });
                    } else {
                        return Single.just(Optional.empty());
                    }
                });
    }





    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String ROLES_KEY_PREFIX = "roles:";
    private static final long REDIS_EXPIRATION_TIME = 3600;

    // Phương thức để lấy danh sách vai trò từ Redis hoặc database nếu không có trong Redis
    // Phương thức để lấy danh sách vai trò từ Redis hoặc database nếu không có trong Redis
    public List<String> getRole(Integer id) {
        String redisKey = ROLES_KEY_PREFIX + id;

        ListOperations<String, String> listOps = redisTemplate.opsForList();

        // Đo thời gian trước khi thực hiện bất kỳ hoạt động nào
        long startTime = System.currentTimeMillis();

        List<String> cachedRoles = listOps.range(redisKey, 0, -1);

        // Đo thời gian khi dữ liệu có trong Redis
        long endTimeRedis = System.currentTimeMillis();

        if (cachedRoles != null && !cachedRoles.isEmpty()) {
            // Trả về dữ liệu từ Redis nếu có sẵn
            System.out.println("Thời gian khi dữ liệu có trong Redis: " + (endTimeRedis - startTime) + " ms");
            return cachedRoles;
        } else {
            // Nếu không có trong Redis, thực hiện câu truy vấn SQL để lấy dữ liệu từ database
            List<Role> databaseRoles = roleRespository.getRoles(id);

            // Đo thời gian khi phải thực hiện câu truy vấn SQL
            long startTimeSQL = System.currentTimeMillis();

            // Chuyển đổi từ List<Role> sang List<String> bằng mapper hoặc một cách nào đó
            List<String> roleResponses = convertToRoleStrings(databaseRoles);

            // Lưu vào Redis để lần sau không cần truy vấn database
            listOps.rightPushAll(redisKey, roleResponses);
            redisTemplate.expire(redisKey, REDIS_EXPIRATION_TIME, TimeUnit.SECONDS); // Đặt thời gian hết hạn

            // Đo thời gian sau khi hoàn tất câu truy vấn SQL
            long endTimeSQL = System.currentTimeMillis();
            System.out.println("Thời gian khi phải thực hiện câu truy vấn SQL: " + (endTimeSQL - startTimeSQL) + " ms");

            return roleResponses;
        }
    }

    @Override
    public Single<MessageResponses> delete(Integer id, Authentication authentication) {
        return checkPermissionDelete(authentication)
                .flatMap(actions -> roleRespository.findbyids(id)
                        .flatMap(users -> {
                            if (users.isPresent()) {
                                return roleRespository.delete(id)
                                        .map(x -> {
                                            MessageResponses messageResponse = new MessageResponses("success");
                                            messageResponse.setItemPermissions(actions);
                                            return messageResponse;
                                        });
                            } else {
                                return Single.just(new MessageResponses("ID does not exist"));
                            }
                        })

                        .onErrorReturnItem(new MessageResponses("error"))
                );
    }

    @Override
    public Single<RoleResponse> insert(RoleRequest request, Authentication authentication) {
        return checkPermissionInsert(authentication)
                .flatMap(actions -> {
                    Role role = mapper.toPojo(request);
                    return roleRespository.addRole(role)
                            .flatMap(po -> {
                                RoleResponse response = mapper.toResponse(po.orElse(null));
                                if (response != null) {
                                    response.setItemPermissions(actions);
                                    return Single.just(response);
                                } else {
                                    return Single.error(new ApiException("resource not found!"));
                                }
                            });
                });
    }

    @Override
    public Single<RoleResponse> update(Integer id, RoleRequest request, Authentication authentication) {
        return checkPermissionUpdate(authentication)
                .flatMap(actions -> roleRespository.findbyids(id)
                        .flatMap(users -> {
                            Role role = mapper.toPojo(request);
                            if (users.isPresent()) {
                                return roleRespository.update(id, role)
                                        .flatMap(updatedUser -> {
                                            RoleResponse response = mapper.toResponse(updatedUser.orElse(null));
                                            if (response != null) {
                                                response.setItemPermissions(actions);
                                                return Single.just(response);
                                            } else {
                                                return Single.error(new ApiException("Failed to update role"));
                                            }
                                        });
                            } else {
                                return Single.error(new ApiException("ID does not exist"));
                            }
                        })
                );
    }

    @Override
    public Single<RoleResponse> detail(Integer id, Authentication authentication) {
        return checkPermissionGet(authentication)
                .flatMap(actions -> getById(id, actions));
    }
    private Single<RoleResponse> getById(Integer id, Set<String> actions) {
        return roleRespository.findbyids(id)
                .flatMap(po -> {
                    RoleResponse response = mapper.toResponse(po.orElse(null));
                    if (response != null) {
                        response.setItemPermissions(actions);
                        return Single.just(response);
                    }
                    return Single.error(new ApiException("resource not found!"));
                })
                .flatMap(this::addExtraInfo);
    }
    public @NonNull Single<RoleResponse> addExtraInfo(RoleResponse rs) {
        return Single.just(rs);
    }


    private List<String> convertToRoleStrings(List<Role> roles) {
        return roles.stream()
                .map(Role::getRoleName) // Chuyển đối từ Role sang tên của vai trò
                .collect(Collectors.toList());
    }

    protected Single<Set<String>> getPermitActions(Authentication authentication) {
        UserAuthorDTO userAuthorDTO = extractUser(authentication);// trich xuat thong tin nguoi dung, lay o trong phan context
        return rolePermisionRepository.getPermissions(getPermissionCode() + "%", userAuthorDTO.getRoles())
                .map(permissions -> permissions.stream()
                        .map(s -> {
                            String[] split = s.split("\\.");
                            if (split.length < 2) return null;
                            return split[1];
                        })
                        .filter(Objects::nonNull)// loc bo gia tri null tu luong
                        .collect(Collectors.toSet()));// lay dc mot set cacs quyen duoc phep
    }
    private Single<Set<String>> checkPermitAction(String view, String message, Authentication authentication) {
        return getPermitActions(authentication)
                .flatMap(actions -> actions.contains(view) ?
                        Single.just(actions) :
                        Single.error(new ApiException(message)));
    }
    protected Single<Set<String>> checkPermissionUpdate(Authentication authentication) {
        return checkPermitAction(UPDATE, "Ban không có quyền update", authentication);
    }

    protected Single<Set<String>> checkPermissionDelete(Authentication authentication) {
        return checkPermitAction(DELETE, "Ban không có quyền xóa", authentication);
    }

    protected Single<Set<String>> checkPermissionInsert(Authentication authentication) {
        return checkPermitAction(ADD, "Ban không có quyền thêm", authentication);
    }

    protected Single<Set<String>> checkPermissionGet(Authentication authentication) {
        return checkPermitAction(VIEW, "Ban không có quyền lấy dữ liệu này", authentication);
    }



}