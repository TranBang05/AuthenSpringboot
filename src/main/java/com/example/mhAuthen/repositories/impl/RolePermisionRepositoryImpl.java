package com.example.demo_mhdigital.repositories.impl;

import com.example.demo_mhdigital.repositories.RolePermisionRepository;
import io.reactivex.rxjava3.core.Single;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.com.app.Tables.*;

@Repository
public class RolePermisionRepositoryImpl implements RolePermisionRepository {

    @Autowired
    DSLContext dslContext;
    public Single<List<String>> getPermissions(String permisionCode, List<String> roles) {
        return Single.just(dslContext
                .select(PERMISION.ACTION_CODE)
                .from(PERMISION)
                .join(ROLE_PERMISION)
                .on(PERMISION.ID.eq(ROLE_PERMISION.PER_ID))
                .join(ROLE)
                .on(ROLE.ID.eq(ROLE_PERMISION.ROLE_ID))
                .where(ROLE.ROLE_NAME.in(roles).and(PERMISION.ACTION_CODE.like(permisionCode)))
                .fetchInto(String.class));
    }
}
