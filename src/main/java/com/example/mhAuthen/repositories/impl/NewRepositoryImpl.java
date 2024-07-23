package com.example.demo_mhdigital.repositories.impl;

import com.example.demo_mhdigital.repositories.NewRepository;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.com.app.tables.pojos.CategoryNew;
import org.com.app.tables.pojos.New;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.com.app.Tables.CATEGORY_NEW;
import static org.com.app.Tables.NEW;

@Repository
public class NewRepositoryImpl implements NewRepository {

    @Autowired
    public DSLContext dslContext;
    @Override
    public Single<Optional<New>> addNew(Integer id, New news) {

        return Single.fromCallable(()->{
            dslContext.insertInto(NEW, NEW.TITLE,NEW.CONTENT,NEW.CATEGORY_NEW_ID, NEW.DESCRIPTION)
                    .values(news.getTitle(), news.getContent(), news.getCategoryNewId(),news.getDescription())
                    .onConflictDoNothing().execute();
            return Optional.of(news);
        }).subscribeOn(Schedulers.io());

    }

    @Override
    public Single<Optional<New>> update(Integer id, New news) {
        return Single.fromCallable(()->{
            dslContext.update(NEW)
                    .set(NEW.ID,news.getId())
                    .set(NEW.TITLE,news.getTitle())
                    .set(NEW.CONTENT,news.getContent())
                    .set(NEW.DESCRIPTION,news.getDescription())
                    .set(NEW.CATEGORY_NEW_ID,news.getCategoryNewId())
                    .where(NEW.ID.eq(id))
                    .execute();
            return Optional.of(news);

        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<New>> getNews() {
        return Single.fromCallable(() -> dslContext.selectFrom(NEW).fetchInto(New.class));
    }

    @Override
    public List<New> findByCategoryId(Integer id) {
        return   dslContext.selectFrom(NEW)
                .where(NEW.CATEGORY_NEW_ID.eq(id)).fetchInto(New.class);
    }

    @Override
    public Single<List<New>> findByCategoryIds(List<Integer> ids) {
        return  Single.just(dslContext.selectFrom(NEW)
                .where(NEW.CATEGORY_NEW_ID.in(ids)).fetchInto(New.class));


    }

    @Override
    public Single<Optional<CategoryNew>> findbyidss(Integer ids) {
        return Single.just(Optional.ofNullable(dslContext.selectFrom(CATEGORY_NEW).where(CATEGORY_NEW.ID.eq(ids))
                .fetchOneInto(CategoryNew.class)));
    }
}
