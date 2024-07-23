package com.example.demo_mhdigital.repositories.impl;

import com.example.demo_mhdigital.repositories.CategoryRepository;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.com.app.tables.pojos.CategoryNew;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.com.app.Tables.CATEGORY_NEW;


@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    public DSLContext dslContext;

    @Override
    public Single<Optional<CategoryNew>> addCategory(CategoryNew categoryNew) {

        return Single.fromCallable(() -> {
            dslContext.insertInto(CATEGORY_NEW, CATEGORY_NEW.TITLE, CATEGORY_NEW.CONTENT, CATEGORY_NEW.POSITION)
                    .values(categoryNew.getTitle(), categoryNew.getContent(), categoryNew.getPosition())
                    .onConflictDoNothing().execute();
            return Optional.of(categoryNew);
        }).subscribeOn(Schedulers.io());

    }

    public Single<Optional<CategoryNew>> update(Integer ids, CategoryNew categoryNew) {
        return Single.fromCallable(() -> {
            dslContext.update(CATEGORY_NEW)
                    .set(CATEGORY_NEW.TITLE, categoryNew.getTitle())
                    .set(CATEGORY_NEW.CONTENT, categoryNew.getContent())
                    .set(CATEGORY_NEW.POSITION, categoryNew.getPosition())
                    .where(CATEGORY_NEW.ID.eq(ids))
                    .execute();

            CategoryNew updatedCategoryNew = dslContext.selectFrom(CATEGORY_NEW)
                    .where(CATEGORY_NEW.ID.eq(ids))
                    .fetchOneInto(CategoryNew.class);

            return Optional.ofNullable(updatedCategoryNew);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Optional<CategoryNew>> delete(Integer id) {
        return Single.fromCallable(() -> {
             dslContext.deleteFrom(CATEGORY_NEW)
                    .where(CATEGORY_NEW.ID.eq(id))
                    .execute();
                return Optional.of(new CategoryNew());
        }).subscribeOn(Schedulers.io());
    }


    @Override
    public Single<List<CategoryNew>> getCategory() {

//        return Single.fromCallable(() -> dslContext.selectFrom(CATEGORY_NEW).fetchInto(CategoryNew.class));
        return Single.just( dslContext.selectFrom(CATEGORY_NEW).fetchInto(CategoryNew.class));
    }

    @Override
    public CategoryNew findById(Integer id) {
        return dslContext.selectFrom(CATEGORY_NEW).where(CATEGORY_NEW.ID.eq(id))
                .fetchOneInto(CategoryNew.class);
    }

    @Override
    public Single<Optional<CategoryNew>> findbyids(Integer ids) {
        return Single.just(Optional.ofNullable(dslContext.selectFrom(CATEGORY_NEW).where(CATEGORY_NEW.ID.eq(ids))
                .fetchOneInto(CategoryNew.class)));
    }
}
