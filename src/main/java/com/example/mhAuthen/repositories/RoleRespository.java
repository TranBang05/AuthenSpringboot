package com.example.demo_mhdigital.repositories;

import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRespository {

    Single<List<Role>> getRoles();

    Single<List<Role>> findById(List<Integer> id);

    Single<Optional<Role>> addRole(Role role);

    Single<Optional<Role>> update(Integer id, Role role);

    Single<Optional<Role>> findbyids(Integer ids);

    Single<List<Role>> findbyid(List<Integer> id);

    Single<Optional<Role>> delete(Integer id);

    List<Role> getRoles(Integer userId);


    Single<List<Role>> getRolesByUserId(Integer userIds);

    Single<List<Role>> getRolesByUserIds(List<Integer> userIds);
    Single<Role> findRoleByName(String roleName);
    Single<Boolean> updateRoleId(Integer userId, List<Integer> roleId);


}
