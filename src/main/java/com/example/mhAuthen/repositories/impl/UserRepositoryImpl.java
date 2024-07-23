package com.example.demo_mhdigital.repositories.impl;

import com.example.demo_mhdigital.repositories.UserRepository;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.com.app.tables.pojos.Role;
import org.com.app.tables.pojos.User;
import org.com.app.tables.pojos.UserRole;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.com.app.Tables.*;


@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    public DSLContext dslContext;

    @Override
    public Single<List<User>> getUsers() {
        return Single.just(dslContext.selectFrom(USER).fetchInto(User.class));

    }

    @Override
    public Single<Optional<User>> addUser(User user) {
        return Single.fromCallable(() -> {
            dslContext.insertInto(USER, USER.NAME,USER.PHONE,USER.EMAIL, USER.PASSWORD)
                    .values(user.getName(),user.getPhone(), user.getEmail(),user.getPassword())
                    .onConflictDoNothing().execute();
            return Optional.of(user);
        }).subscribeOn(Schedulers.io());
    }



    @Override
    public Single<Optional<User>> update(Integer id, User user) {
        return Single.fromCallable(() -> {
            dslContext.update(USER)
                    .set(USER.NAME, user.getName())
                    .set(USER.PHONE,user.getPhone())
                    .set(USER.EMAIL,user.getEmail())
                    .set(USER.PASSWORD,user.getPassword())
                    .where(USER.ID.eq(id))
                    .execute();

            User updateUser = dslContext.selectFrom(USER)
                    .where(USER.ID.eq(id))
                    .fetchOneInto(User.class);

            return Optional.ofNullable(updateUser);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Optional<User>> findbyids(Integer ids) {
        if (ids == null) return Single.just(Optional.empty());
        return Single.just(Optional.ofNullable(dslContext.selectFrom(USER).where(USER.ID.eq(ids))
                .fetchOneInto(User.class)));
    }

    @Override
    public Single<UserRole> addUserRole(UserRole userRole) {
        return Single.fromCallable(() -> {
            dslContext.insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE_ID)
                    .values(userRole.getUserId(), userRole.getRoleId())
                    .onConflictDoNothing()
                    .execute();
            return userRole;
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Optional<User>> delete(Integer id) {
        return Single.fromCallable(() -> {

            dslContext.deleteFrom(USER_ROLE)
                    .where(USER_ROLE.USER_ID.eq(id))
                    .execute();

            dslContext.delete(USER)
                    .where(USER.ID.eq(id))
                    .execute();
            return Optional.of(new User());
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Optional<User>> getById(Integer id) {
        return Single.just(Optional.ofNullable(dslContext.selectFrom(USER).where(USER.ID.eq(id))
                .fetchOneInto(User.class)));
    }

    @Override
    public Single<Optional<User>> getByIds(Integer id) {
        return null;
    }

    public Single<List<Role>> getRoles(Integer userId) {
        return Single.just(dslContext
                .select(ROLE.ROLE_NAME)
                .from(ROLE)
                .join(USER_ROLE)
                .on(ROLE.ID.eq(USER_ROLE.ROLE_ID))
                .join(USER)
                .on(USER.ID.eq(USER_ROLE.USER_ID))
                .where(USER.ID.eq(userId))
                .fetchInto(Role.class)
        );
    }

    public Single<User> findByPhone(String phone) {
        return Single.just( dslContext.selectFrom(USER)
                .where(USER.PHONE.eq(phone))
                .fetchOneInto(User.class));
    }

    public Single<List<String>> getListRoleName(Integer user_id) {
        return Single.just( dslContext.select(ROLE.ROLE_NAME)
                .from(USER_ROLE)
                .join(ROLE)
                .on(USER_ROLE.ROLE_ID.eq(ROLE.ID))
                .where(USER_ROLE.USER_ID.eq(user_id))
                .fetchInto(String.class)
        );
    }

}
