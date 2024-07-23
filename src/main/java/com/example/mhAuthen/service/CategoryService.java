package com.example.demo_mhdigital.service;

import com.example.demo_mhdigital.data.request.CategoryNewRequest;
import com.example.demo_mhdigital.data.response.CategoryNewResponse;
import com.example.demo_mhdigital.data.response.NewResponse;
import io.reactivex.rxjava3.core.Single;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    /**
     *
     * them 1 category
     * @return
     */
    Single<String> addCategory(CategoryNewRequest categoryNewRequest);

    /**
     * update category theo categoryId
     */
    Single<String> updateCategory(Integer id, CategoryNewRequest categoryNewRequest);

    Single<String> deleteCategory(Integer id);
    /**
     * LAY DANH DACH CATEGORY, CO KEM THEO 1 LIST NEWS
     * @return
     */
    Single<List<CategoryNewResponse>> getCategory();

    Optional<CategoryNewResponse>findById(Integer id);

    List<NewResponse> getNewsByCategory(Integer categoryId);

    List<NewResponse> findByCategoryId(Integer id);


}
