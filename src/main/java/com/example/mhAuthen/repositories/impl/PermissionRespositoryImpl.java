package com.example.demo_mhdigital.repositories.impl;

import com.example.demo_mhdigital.repositories.PermissionRespository;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.com.app.tables.pojos.Permision;
import org.com.app.tables.pojos.RolePermision;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.com.app.Tables.*;

@Repository

public class PermissionRespositoryImpl implements PermissionRespository {

    @Autowired
    public DSLContext dslContext;
    @Override
    public Single<Optional<Permision>> addPermission(Permision permision) {
        return Single.fromCallable(() -> {
            dslContext.insertInto(PERMISION,PERMISION.ACTION_NAME,PERMISION.ACTION_CODE,PERMISION.DESCRIPTION)
                    .values(permision.getActionName(), permision.getActionCode(), permision.getDescription())
                    .onConflictDoNothing().execute();
            return Optional.of(permision);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Permision>> findByRoleId(List<Integer> id) {
        return Single.fromCallable(() ->
                dslContext.select(PERMISION.ID, PERMISION.ACTION_NAME, PERMISION.ACTION_CODE, PERMISION.DESCRIPTION)
                        .from(PERMISION)
                        .join(ROLE_PERMISION).on(PERMISION.ID.eq(ROLE_PERMISION.PER_ID))
                        .where(ROLE_PERMISION.ROLE_ID.in(id))
                        .fetchInto(Permision.class)
        );
    }

    @Override
    public Single<List<Permision>> findByPerId(List<Integer> id) {
        return Single.fromCallable(() ->
                dslContext.select(ROLE.ID,ROLE.ROLE_NAME).from(ROLE)
                        .join(ROLE_PERMISION).on(ROLE.ID.eq(ROLE_PERMISION.ROLE_ID))
                        .where(ROLE_PERMISION.PER_ID.in(id))
                        .fetchInto(Permision.class)
        );
    }

    @Override
    public Single<List<Permision>> getPermissionByRoleId(Integer roleIds) {
        if (roleIds == null)  return Single.just(Collections.emptyList());
        return Single.fromCallable(() ->
                dslContext.select(PERMISION.ID, PERMISION.ACTION_NAME, PERMISION.ACTION_CODE, PERMISION.DESCRIPTION)
                        .from(PERMISION)
                        .join(ROLE_PERMISION).on(PERMISION.ID.eq(ROLE_PERMISION.PER_ID))
                        .where(ROLE_PERMISION.ROLE_ID.in(roleIds))
                        .fetchInto(Permision.class)
        );
    }





    @Override
    public Single<Boolean> updatePermissionId(Integer roleId, List<Integer> perIds) {
        return Single.fromCallable(() ->
                        dslContext.deleteFrom(ROLE_PERMISION)
                                .where(ROLE_PERMISION.ROLE_ID.eq(roleId))
                                .execute() >= 0)
                .flatMap(deletionSuccess -> {

            if (deletionSuccess) {
                List<Query> insertQueries = perIds.stream()
                        .map(perId -> dslContext.insertInto(ROLE_PERMISION)
                                .set(ROLE_PERMISION.ROLE_ID, roleId)
                                .set(ROLE_PERMISION.PER_ID, perId))
                        .collect(Collectors.toList());

                return Single.fromCallable(() -> {
                    dslContext.batch(insertQueries).execute();
                    return true;
                });
            } else {
                return Single.error(new Exception("Failed"));
            }
        });
    }
    @Override
    public Single<List<Permision>> getPermissionByRoleIds(List<Integer> roleIds) {
        return Single.fromCallable(() ->
                dslContext.select(PERMISION.ID, PERMISION.ACTION_NAME, PERMISION.ACTION_CODE, PERMISION.DESCRIPTION)
                        .from(PERMISION)
                        .join(ROLE_PERMISION).on(PERMISION.ID.eq(ROLE_PERMISION.PER_ID))
                        .where(ROLE_PERMISION.ROLE_ID.in(roleIds))
                        .fetchInto(Permision.class)
        );
    }

}
