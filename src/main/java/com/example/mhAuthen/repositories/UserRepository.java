package com.example.demo_mhdigital.repositories;

import com.example.demo_mhdigital.data.request.UserRequest;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.User;
import org.com.app.tables.pojos.UserRole;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Single<List<User>> getUsers();

    Single<Optional<User>> addUser(User user);

    Single<Optional<User>> update(Integer id, User user);

    Single<Optional<User>> findbyids(Integer ids);

    Single<UserRole> addUserRole(UserRole userRole);
    Single<Optional<User>> delete(Integer id);

    Single<Optional<User>> getById(Integer id);

    Single<Optional<User>> getByIds(Integer id);

    Single<User> findByPhone(String phone);

    Single<List<String>> getListRoleName(Integer user_id);
}


