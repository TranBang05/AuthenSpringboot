package com.example.demo_mhdigital.service.impl;


import com.example.demo_mhdigital.config.authconfig.JwtTokenUtil;
import com.example.demo_mhdigital.config.exception.ApiException;

import com.example.demo_mhdigital.data.request.ChangePasswordRequest;
import com.example.demo_mhdigital.data.request.UserRequest;
import com.example.demo_mhdigital.data.mappers.PermissionMapper;
import com.example.demo_mhdigital.data.mappers.RoleMapper;
import com.example.demo_mhdigital.data.mappers.UserMapper;
import com.example.demo_mhdigital.data.request.UserRoleRequest;
import com.example.demo_mhdigital.data.response.MessageResponses;
import com.example.demo_mhdigital.data.response.user.PermissionResponse;
import com.example.demo_mhdigital.data.response.user.RoleResponse;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import com.example.demo_mhdigital.data.user.AuthRequest;
import com.example.demo_mhdigital.data.user.TokenResponse;
import com.example.demo_mhdigital.data.user.UserAuthorDTO;
import com.example.demo_mhdigital.repositories.PermissionRespository;
import com.example.demo_mhdigital.repositories.RolePermisionRepository;
import com.example.demo_mhdigital.repositories.RoleRespository;
import com.example.demo_mhdigital.repositories.UserRepository;
import com.example.demo_mhdigital.service.UserService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.Role;
import org.com.app.tables.pojos.User;
import org.com.app.tables.pojos.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo_mhdigital.config.utils.SecurityUtils.extractUser;
import static com.example.demo_mhdigital.data.constant.ActionConstant.*;
import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper mapper;

    @Autowired
    RoleRespository roleRespository;

    @Autowired
    RoleMapper mappers;

    @Autowired
    PermissionRespository permissionRespository;


    @Autowired
    PermissionMapper perMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RolePermisionRepository rolePermisionRepository;

    protected String getPermissionCode() {
        return "user";
    }
    @Override
    public Single<List<UserResponse>> getUsers() {
        return userRepository.getUsers()
                .flatMap(userList -> {
                    List<UserResponse> userResponses = mapper.toResponses(userList);
                    List<Integer> userIds = userResponses.stream()
                            .map(UserResponse::getId)
                            .toList();
                    return roleRespository.findById(userIds)
                            .map(roleList -> {
                                List<RoleResponse> roleResponses = mappers.toResponses(roleList);
                                for (UserResponse userResponse : userResponses) {
                                    List<RoleResponse> userRoleResponses = roleResponses.stream()
                                            .filter(role -> role.getId().equals(userResponse.getId()))
                                            .collect(Collectors.toList());
                                    userResponse.setRoleResponses(userRoleResponses);
                                }
                                return userResponses;
                            });
                });
    }
/*
    public Single<List<UserResponse>> getUsers() {
        return userRepository.getUsers()
                .flatMap(userList -> {
                    List<UserResponse> userResponses = mapper.toResponses(userList);
                    List<Integer> userIds = userResponses.stream()
                            .map(UserResponse::getId)
                            .toList();

                    return roleRespository.findById(userIds)
                            .flatMap(roleList -> {
                                List<RoleResponse> roleResponses = mappers.toResponses(roleList);
                                List<Integer> roleIds = roleResponses.stream()
                                        .map(RoleResponse::getId)
                                        .toList();

                                return permissionRespository.findByRoleId(roleIds)
                                        .map(permissionList -> {
                                            List<PermissionResponse> permissionResponses = perMapper.toResponses(permissionList);

                                            Map<Integer, List<RoleResponse>> roleResponseMap = roleResponses.stream()
                                                    .filter(roleResponse -> roleResponse.getId() != null)
                                                    .collect(Collectors.groupingBy(RoleResponse::getId));

                                            Map<Integer, List<PermissionResponse>> permissionResponseMap = permissionResponses.stream()
                                                    .filter(permissionResponse -> permissionResponse.getId() != null)
                                                    .collect(Collectors.groupingBy(PermissionResponse::getId));

                                            roleResponses.forEach(roleResponse -> {
                                                List<PermissionResponse> permissions = permissionResponseMap.getOrDefault(roleResponse.getId(), new ArrayList<>());
                                                roleResponse.setPermisionResponses(permissions);
                                            });

                                            userResponses.forEach(userResponse -> {
                                                List<RoleResponse> roles = roleResponseMap.getOrDefault(userResponse.getId(), new ArrayList<>());
                                                userResponse.setRoleResponses(roles);
                                            });

                                            return userResponses;
                                        });
                            });
                });
    }*/

    @Override
    public Single<String> addUser(UserRequest userRequest) {
        User user = mapper.toPojo(userRequest);

        return userRepository.addUser(user)
                .map(userInsert -> {
                    if (userInsert.isPresent()) {
                        return "success";
                    } else {
                        throw new RuntimeException("error");
                    }
                }).onErrorReturnItem("error");
    }

    @Override
    public Single<String> updateUser(Integer id, UserRequest userRequest) {
        User user = mapper.toPojo(userRequest);
        return userRepository.findbyids(id)
                .flatMap(users -> {
                    if (users.isPresent()) {
                        return userRepository.update(id, user)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                });
    }

    @Override
    public Single<String> deleteUser(Integer id) {
        return userRepository.findbyids(id)
                .flatMap(users -> {
                    if (users.isPresent()) {
                        return userRepository.delete(id)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                });


    }


    @Override
    public  Single<Optional<UserResponse>> getById(Integer id) {
        return userRepository.findbyids(id)
                .flatMap(userOptional -> {
                    UserResponse response = mapper.toResponse(userOptional.orElse(null));
                    if (response != null) {
                        return roleRespository.getRolesByUserId(id)
                                .flatMap(roleList -> {
                                    List<RoleResponse> roleResponses = mappers.toResponses(roleList);

                                    List<Integer> roleIds = roleResponses.stream()
                                            .map(RoleResponse::getId)
                                            .toList();

                                    List<Single<List<PermissionResponse>>> permission = roleIds.stream()
                                            .map(roleId -> permissionRespository.getPermissionByRoleId(roleId)
                                                    .map(permissionList -> perMapper.toResponses(permissionList)))
                                            .collect(Collectors.toList());

                                    return Single.zip(permission, ob -> {
                                        Map<Integer, List<PermissionResponse>> permissionResponseMap = new HashMap<>();
                                        for (int i = 0; i < ob.length; i++) {
                                            List<PermissionResponse> permissionResponses = (List<PermissionResponse>) ob[i];
                                            permissionResponseMap.put(roleIds.get(i), permissionResponses);
                                        }


                                        response.setRoleResponses(roleResponses);

                                        roleResponses.stream()
                                                .peek(res-> {
                                                    res.setPermisionResponses(permissionResponseMap.getOrDefault(res.getId(),null));
                                                }).collect(Collectors.toList());

                                        return Optional.of(response);
                                    });
                                });
                    } else {
                        return Single.just(Optional.empty());
                    }
                });
    }



    public Single<MessageResponses> changePassword(ChangePasswordRequest request) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return userRepository.findbyids(request.getId())
                .flatMap(user -> {
                    if (passwordEncoder.matches(request.getOldPassword(), user.orElse(null).getPassword())) {
                        user.get().setPassword(passwordEncoder.encode(request.getPassword()));
                        return userRepository.update(user.get().getId(), user.get())
                                .flatMap(user1 -> Single.just(new MessageResponses("Success")));
                    }
                    return Single.error(new ApiException("mật khẩu cũ không đúng", HttpStatus.OK.value()));
                });
    }

    @Override
    public Single<String> updateUserRole(Integer id, UserRequest userRoleRequest, Authentication authentication) {
        return  checkPermissionUpdate(authentication)
                .flatMap(actions -> {
        User user = mapper.toPojo(userRoleRequest);
        return userRepository.findbyids(id)
                .flatMap(users -> {
                    if (users.isPresent()) {
                        return roleRespository.findbyid(userRoleRequest.getRoleId())
                                .flatMap(role -> {
                                    if (role == null || role.isEmpty()) {
                                        return Single.just("Role ID does not exist");
                                    } else {
                                        return roleRespository.updateRoleId(id, userRoleRequest.getRoleId())
                                                .flatMap(roleUpdate -> {
                                                    if (roleUpdate != null) {
                                                        return userRepository.update(id, user)
                                                                .map(x -> "Update Role Success")
                                                                .onErrorReturnItem("error");
                                                    } else {
                                                        return Single.just("Failed to update role ID");
                                                    }
                                                });
                                    }
                                });
                    } else {
                        return Single.just("UserID does not exist");
                    }
                });
                });
    }
    @Override
    public Single<UserResponse> detail(Integer id, Authentication authentication) {
        return checkPermissionGet(authentication)
                .flatMap(actions -> getById(id, actions));
    }
   /* private Single<UserResponse> getById(Integer id, Set<String> actions) {
        return userRepository.findbyids(id)
                .flatMap(po -> {
                    UserResponse response = mapper.toResponse(po.orElse(null));
                    if (response != null) {
                        response.setItemPermissions(actions);
                        return Single.just(response);
                    }
                    return Single.error(new ApiException("resource not found!"));
                })
                .flatMap(this::addExtraInfo);
    }*/

    private Single<UserResponse> getById(Integer id, Set<String> actions) {
        return userRepository.findbyids(id)
                .flatMap(po -> {
                    UserResponse response = mapper.toResponse(po.orElse(null));
                    if (response != null) {
                        response.setItemPermissions(actions);
                        return roleRespository.getRolesByUserId(response.getId())
                                .flatMap(roleList -> {
                                    if (roleList.isEmpty()) {
                                        // Bắt lỗi khi không tìm thấy role
                                        return Single.just(response);
                                    }
                                    List<RoleResponse> roleResponses = mappers.toResponses(roleList);
                                    response.setRoleResponses(roleResponses);
                                    List<Single<List<PermissionResponse>>> permissionRequests = roleResponses.stream()
                                            .map(roleResponse -> permissionRespository.getPermissionByRoleId(roleResponse.getId())
                                                    .map(permissionList -> perMapper.toResponses(permissionList)))
                                            .collect(Collectors.toList());

                                    return Single.zip(permissionRequests, permissions -> {
                                        for (int i = 0; i < permissions.length; i++) {
                                            List<PermissionResponse> permissionResponses = (List<PermissionResponse>) permissions[i];
                                            roleResponses.get(i).setPermisionResponses(permissionResponses);
                                        }
                                        return response;
                                    });
                                })
                                .flatMap(this::addExtraInfo);
                    } else {
                        return Single.error(new ApiException("User không tồn tại!"));
                    }
                });
    }

    public @NonNull Single<UserResponse> addExtraInfo(UserResponse rs) {
        return Single.just(rs);
    }
    @Override
    public Single<TokenResponse> authenticate(AuthRequest request) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return userRepository.findByPhone(request.getPhone())
                .flatMap(user -> {
                    if (user == null)
                        return Single.error(new ApiException("not found user!", HttpStatus.BAD_REQUEST.value()));

                    return passwordEncoder.matches(request.getPassword(), user.getPassword()) ?
                            Single.just(user) :
                            Single.error(new ApiException("invalid password!", HttpStatus.BAD_REQUEST.value()));
                })
                .flatMap(user ->  userRepository.getListRoleName(user.getId())
                        .map(roles -> {
                            String token = jwtTokenUtil.generateAccessToken(user, roles);
                            return new TokenResponse(token);
                        }));
    }

    @Override
    public Single<MessageResponses> delete(Integer id, Authentication authentication) {
        return checkPermissionDelete(authentication)
                .flatMap(actions -> userRepository.findbyids(id)
                        .flatMap(users -> {
                            if (users.isPresent()) {
                                return userRepository.delete(id)
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
    public Single<UserResponse> insert(UserRequest request, Authentication authentication) {
        return checkPermissionInsert(authentication)
                .flatMap(actions -> {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encryptedPwd = passwordEncoder.encode(request.getPassword());
                    request.setPassword(encryptedPwd);
                    User user = mapper.toPojo(request);
                    return userRepository.addUser(user)
                            .flatMap(po -> {
                                UserResponse response = mapper.toResponse(po.orElse(null));
                                if (response != null) {
                                    response.setItemPermissions(actions);
                                    return Single.just(response);
                                } else {
                                    return Single.error(new ApiException("resource not found!"));
                                }
                            });
                });
    }


    public Single<UserResponse> update(Integer id, UserRequest request, Authentication authentication) {
        return checkPermissionUpdate(authentication)
                .flatMap(actions -> userRepository.findbyids(id)
                        .flatMap(users -> {
                            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                            String encryptedPwd = passwordEncoder.encode(request.getPassword());
                            request.setPassword(encryptedPwd);
                            User user = mapper.toPojo(request);
                            if (users.isPresent()) {
                                return userRepository.update(id, user)
                                        .flatMap(updatedUser -> {
                                            UserResponse response = mapper.toResponse(updatedUser.orElse(null));
                                            if (response != null) {
                                                response.setItemPermissions(actions);
                                                return Single.just(response);
                                            } else {
                                                return Single.error(new ApiException("Failed to update user"));
                                            }
                                        });
                            } else {
                                return Single.error(new ApiException("ID does not exist"));
                            }
                        })
                );
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
