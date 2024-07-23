package com.example.demo_mhdigital.service;

import com.example.demo_mhdigital.data.request.CategoryNewRequest;
import com.example.demo_mhdigital.data.request.NewRequest;
import com.example.demo_mhdigital.data.response.CategoryNewResponse;
import com.example.demo_mhdigital.data.response.NewResponse;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface NewService {

    Single<String> addNew(Integer id,NewRequest newRequest);

    Single<String> updateNew(Integer id, NewRequest newRequest);

    Single<List<NewResponse>> getNews();

    List<NewResponse> findByCategoryId(Integer id);

    Optional<CategoryNewResponse> findById(Integer id);
}
