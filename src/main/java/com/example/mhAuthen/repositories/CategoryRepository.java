package com.example.demo_mhdigital.repositories;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.CategoryNew;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Single<Optional<CategoryNew>> addCategory(CategoryNew categoryNew);

    Single<Optional<CategoryNew>> update(Integer id, CategoryNew categoryNew);

    Single<Optional<CategoryNew>> delete(Integer id);

    Single<List<CategoryNew>> getCategory();

    CategoryNew findById(Integer id);

    Single<Optional<CategoryNew>> findbyids(Integer ids);
}
