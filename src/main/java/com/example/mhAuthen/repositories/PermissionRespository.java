package com.example.demo_mhdigital.repositories;

import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.Permision;

import java.util.List;
import java.util.Optional;

public interface PermissionRespository {
    Single<Optional<Permision>> addPermission(Permision permision);

    Single<List<Permision>> findByRoleId (List<Integer> id);

    Single<List<Permision>> findByPerId (List<Integer> id);



    Single<List<Permision>> getPermissionByRoleId(Integer roleIds);

    Single<Boolean> updatePermissionId(Integer roleId, List<Integer> perId);

    Single<List<Permision>> getPermissionByRoleIds(List<Integer> roleIds);
}
