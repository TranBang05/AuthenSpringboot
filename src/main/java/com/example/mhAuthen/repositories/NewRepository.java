package com.example.demo_mhdigital.repositories;



import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.CategoryNew;
import org.com.app.tables.pojos.New;

import java.util.List;
import java.util.Optional;

public interface NewRepository {

    Single<Optional<New>> addNew(Integer id, New news);

    Single<Optional<New>> update(Integer id, New news);

    Single<List<New>> getNews();

    List<New> findByCategoryId(Integer id);

    Single<List<New>> findByCategoryIds (List<Integer> ids);

    Single<Optional<CategoryNew>> findbyidss(Integer ids);
}
