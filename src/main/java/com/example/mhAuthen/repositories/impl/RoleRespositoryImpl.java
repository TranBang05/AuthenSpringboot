package com.example.demo_mhdigital.repositories.impl;

import com.example.demo_mhdigital.repositories.RoleRespository;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.com.app.tables.pojos.Role;
import org.com.app.tables.pojos.User;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.com.app.Tables.*;
import static org.com.app.Tables.USER;

@Repository
public class RoleRespositoryImpl implements RoleRespository {

    @Autowired
    public DSLContext dslContext;

    @Override
    public Single<List<Role>> getRoles() {
        return Single.just(dslContext.selectFrom(ROLE).fetchInto(Role.class));
    }

    @Override
    public Single<List<Role>> findById(List<Integer> id) {
        return Single.fromCallable(() ->
                dslContext.select(ROLE.fields())
                        .from(ROLE)
                        .join(USER_ROLE).on(USER_ROLE.ROLE_ID.eq(ROLE.ID))
                        .where(USER_ROLE.USER_ID.in(id))
                        .fetchInto(Role.class)
        );
    }

    @Override
    public Single<Optional<Role>> addRole( Role role) {
        return Single.fromCallable(() -> {
            dslContext.insertInto(ROLE,ROLE.ROLE_NAME, ROLE.ROLE_CODE, ROLE.DESCRIPTION)
                    .values(role.getRoleName(), role.getRoleCode(), role.getDescription())
                    .onConflictDoNothing().execute();
            return Optional.of(role);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Optional<Role>> update(Integer id, Role role) {
        return Single.fromCallable(() -> {
            dslContext.update(ROLE)
                    .set(ROLE.ROLE_NAME, role.getRoleName())
                    .set(ROLE.ROLE_CODE,role.getRoleCode())
                    .set(ROLE.DESCRIPTION,role.getDescription())
                    .where(ROLE.ID.eq(id))
                    .execute();

            Role updateRole = dslContext.selectFrom(ROLE)
                    .where(ROLE.ID.eq(id))
                    .fetchOneInto(Role.class);
            return Optional.ofNullable(updateRole);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Optional<Role>> findbyids(Integer ids) {
        return Single.just(Optional.ofNullable(dslContext.selectFrom(ROLE).where(ROLE.ID.eq(ids))
                .fetchOneInto(Role.class)));

    }

    @Override
    public Single<List<Role>> getRolesByUserId(Integer userIds) {
        return Single.fromCallable(() ->
                dslContext.select(ROLE.ID, ROLE.ROLE_NAME, ROLE.ROLE_CODE, ROLE.DESCRIPTION)
                        .from(ROLE)
                        .join(USER_ROLE).on(ROLE.ID.eq(USER_ROLE.ROLE_ID))
                        .where(USER_ROLE.USER_ID.in(userIds))
                        .fetchInto(Role.class)
        );
    }

    @Override
    public Single<List<Role>> getRolesByUserIds(List<Integer> userIds) {
        return Single.fromCallable(() ->
                dslContext.select(ROLE.ID, ROLE.ROLE_NAME, ROLE.ROLE_CODE, ROLE.DESCRIPTION)
                        .from(ROLE)
                        .join(USER_ROLE).on(ROLE.ID.eq(USER_ROLE.ROLE_ID))
                        .where(USER_ROLE.USER_ID.in(userIds))
                        .fetchInto(Role.class)
        );
    }

    @Override
    public Single<List<Role>> findbyid(List<Integer> ids) {
        /*if (ids == null || ids.isEmpty()) {
            return Single.just(Collections.emptyList());
        }*/
        return Single.fromCallable(() -> dslContext.selectFrom(ROLE)
                .where(ROLE.ID.in(ids))
                .fetchInto(Role.class));
    }

    @Override
    public Single<Optional<Role>> delete(Integer id) {
        return Single.fromCallable(() -> {

            dslContext.deleteFrom(USER_ROLE)
                    .where(USER_ROLE.ROLE_ID.eq(id))
                    .execute();

            dslContext.deleteFrom(ROLE)
                    .where(ROLE.ID.eq(id))
                    .execute();
            return Optional.of(new Role());
        }).subscribeOn(Schedulers.io());
    }

    public Single<Role> findRoleByName(String roleName) {
        return Single.just( dslContext.selectFrom(ROLE)
                .where(ROLE.ROLE_NAME.eq(roleName))
                .fetchOneInto(Role.class));
    }

    @Override
    public Single<Boolean> updateRoleId(Integer userId, List<Integer> roleId) {
        return Single.fromCallable(() ->
                        dslContext.deleteFrom(USER_ROLE)
                                .where(USER_ROLE.USER_ID.eq(userId))
                                .execute() >= 0)
                .flatMap(deletionSuccess -> {

                    if (deletionSuccess) {
                        List<Query> insertQueries = roleId.stream()
                                .map(rolesId -> dslContext.insertInto(USER_ROLE)
                                        .set(USER_ROLE.USER_ID,userId)
                                        .set(USER_ROLE.ROLE_ID, rolesId))
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

    //tên của các vai trò từ bảng ROLE dựa trên ID của người dùng được cung cấp
    public List<Role> getRoles(Integer userId) {
        return dslContext
                .select(ROLE.ROLE_NAME)
                .from(ROLE)
                .join(USER_ROLE)
                .on(ROLE.ID.eq(USER_ROLE.ROLE_ID))
                .join(USER)
                .on(USER.ID.eq(USER_ROLE.USER_ID))
                .where(USER.ID.eq(userId))
                .fetchInto(Role.class)
        ;
    }



}
