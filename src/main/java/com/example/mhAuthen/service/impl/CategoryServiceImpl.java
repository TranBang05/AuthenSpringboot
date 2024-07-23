package com.example.demo_mhdigital.service.impl;

import com.example.demo_mhdigital.data.request.CategoryNewRequest;
import com.example.demo_mhdigital.data.response.CategoryNewResponse;
import com.example.demo_mhdigital.data.response.NewResponse;
import com.example.demo_mhdigital.data.mappers.CategoryNewMapper;
import com.example.demo_mhdigital.data.mappers.NewMapper;
import com.example.demo_mhdigital.repositories.CategoryRepository;
import com.example.demo_mhdigital.repositories.NewRepository;
import com.example.demo_mhdigital.service.CategoryService;
import org.com.app.tables.pojos.CategoryNew;
import org.com.app.tables.pojos.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.reactivex.rxjava3.core.Single;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    NewRepository newRepository;

    @Autowired
    CategoryNewMapper mapper;

    @Autowired
    NewMapper mappers;


    @Override
    public Single<String> addCategory(CategoryNewRequest categoryNewRequest) {
        CategoryNew categoryNew = mapper.toPojo(categoryNewRequest);

        return categoryRepository.addCategory(categoryNew)
                .map(categoryInsert -> {
                    if (categoryInsert != null) {
                        return "success";
                    } else {
                        throw new RuntimeException("error");
                    }
                }).onErrorReturnItem("error");
    }

    @Override
    public Single<String> updateCategory(Integer id, CategoryNewRequest categoryNewRequest) {
        CategoryNew categoryNew = mapper.toPojo(categoryNewRequest);
        return categoryRepository.findbyids(id)
                .flatMap(category -> {
                    if (category.isPresent()) {
                        return categoryRepository.update(id, categoryNew)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                });
    }

    @Override
    public Single<String> deleteCategory(Integer id) {
        return categoryRepository.findbyids(id)
                .flatMap(category -> {
                    if (category.isPresent()) {
                        return categoryRepository.delete(id)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                });
    }


    @Override
    public Single<List<CategoryNewResponse>> getCategory() {

//        return categoryRepository.getCategory()
//                .map(list -> {
//                    List<CategoryNewResponse> categoryResponses = mapper.toResponses(list);
//
//                    List<Integer> categoryIds = categoryResponses.stream()
//                            .map(CategoryNewResponse::getCategoryNewId)
//                            .collect(Collectors.toList());
//
//                    List<New> newsList = newRepository.findByCategoryIds(categoryIds);
//                    List<NewResponse> newResponses = mappers.toResp(newsList);
//
//gf
//
//                    Map<Integer, List<NewResponse>> newResponseMap = newResponses.stream()
//                            .filter(response -> response.getCategoryNewId() != null)
//                            .collect(Collectors.groupingBy(NewResponse::getCategoryNewId));
//                    categoryResponses.stream()
//                            .peek(res ->{
//                                res.setNewResponses(newResponseMap.getOrDefault(res.getCategoryNewId(), null));
//                            })
//                            .collect(Collectors.toList());
//
//
//                    return categoryResponses;
//                });
        return categoryRepository.getCategory()
                .flatMap(list -> {
                    List<CategoryNewResponse> categoryResponses = mapper.toResponses(list);
                    List<Integer> categoryIds = categoryResponses.stream()
                            .map(CategoryNewResponse::getCategoryNewId)
                            .collect(Collectors.toList());

                    return newRepository.findByCategoryIds(categoryIds)
                            .map(newsList -> {
                                List<NewResponse> newResponses = mappers.toResp(newsList);
                                Map<Integer, List<NewResponse>> newResponseMap = newResponses.stream()
                                        .filter(response -> response.getCategoryNewId() != null)
                                        .collect(Collectors.groupingBy(NewResponse::getCategoryNewId));
                                categoryResponses.stream()
                                        .peek(res -> {
                                            res.setNewResponses(newResponseMap.getOrDefault(res.getCategoryNewId(), null));
                                        })
                                        .collect(Collectors.toList());
                                return categoryResponses;
                            });
                });



//        List<CategoryNew> categoryList = categoryRepository.getCategory();
//        List<CategoryNewResponse> categoryResponses = mapper.toResponses(categoryList);
//
////        for (CategoryNewResponse response : categoryResponses) {
////            List<New> newsList = newRepository.findByCategoryId(response.getCategoryNewId());
////            List<NewResponse> newResponses = mappers.toResp(newsList);
////            response.setNewResponses(newResponses);
////        }
////
////        return categoryResponses;
//
//        /**
//         * java 8
//         */
//        List<Integer> categoryIds = categoryResponses.stream()
//                .map(
//                        CategoryNewResponse::getCategoryNewId
//                ).collect(Collectors.toList());
//        List<New> newsList = newRepository.findByCategoryIds(categoryIds);
//        List<NewResponse> newResponses = mappers.toResp(newsList);
//
//        //key la ccategoryId
////        Map<Integer, List<NewResponse>> newResponseMap = newResponses
////                .stream().collect(Collectors.groupingBy(NewResponse::getCategoryNewId));
//
//        Map<Integer, List<NewResponse>> newResponseMap = newResponses.stream()
//                .filter(response -> response.getCategoryNewId() != null) // Filter out null elements
//                .collect(Collectors.groupingBy(NewResponse::getCategoryNewId));
////        for(NewResponse news :newResponses){
////            int categoryId=news.getCategoryNewId();
////            if(!newResponseMap.containsKey(categoryId)){
////               newResponseMap.put(categoryId, new ArrayList<>());
////            }else {
////                newResponseMap.get(categoryId).add(news);
////            }
////        }
//
//
//        categoryResponses.stream()
//                .peek(res ->{
//                    //dung code tren
//                    res.setNewResponses(newResponseMap.getOrDefault(res.getCategoryNewId(), null));
////                    res.setNewResponses(newResponseMap.get(res.getCategoryNewId())); // k dung
//                })
//                .collect(Collectors.toList());
//        return categoryResponses;

        /*List<CategoryNew> categoryList = categoryRepository.getCategory();
        List<CategoryNewResponse> categoryResponses = mapper.toResponses(categoryList);

        List<Integer> categoryIds = categoryResponses.stream()
                .map(CategoryNewResponse::getCategoryNewId)
                .collect(Collectors.toList());

        List<New> newsList = newRepository.findByCategoryIds(categoryIds);
        List<NewResponse> newResponses = mappers.toResp(newsList);



        Map<Integer, List<NewResponse>> newResponseMap = newResponses.stream()
                .filter(response -> response.getCategoryNewId() != null)
                .collect(Collectors.groupingBy(NewResponse::getCategoryNewId));



        categoryResponses.stream()
               .peek(res ->{
                    //dung code tren
                    res.setNewResponses(newResponseMap.getOrDefault(res.getCategoryNewId(), null));
                   //res.setNewResponses(newResponseMap.get(res.getCategoryNewId())); // k dung

               })
              .collect(Collectors.toList());
      return categoryResponses;
*/

    }


    @Override
    public List<NewResponse> findByCategoryId(Integer id) {

        List<New> list = newRepository.findByCategoryId(id);
        List<NewResponse> responses=mappers.toResp(list);
        return responses;

    }

    @Override
    public Optional<CategoryNewResponse> findById(Integer id) {

        Optional<CategoryNew> category = Optional.ofNullable(categoryRepository.findById(id));
        if (category.isPresent()) {
            CategoryNewResponse response = mapper.toResponse(category.get());
            List<New> newsList = newRepository.findByCategoryId(id);
            List<NewResponse> newResponses = mappers.toResp(newsList);
            response.setNewResponses(newResponses);
            return Optional.of(response);
        } else {
            return Optional.empty();

        /*return categoryRepository.findById(id)
                .flatMap(category -> {
                    if (category.isPresent()) {
                        CategoryNewResponse response = mapper.toResponse(category.get());
                        List<New> newsList = newRepository.findByCategoryId(id);
                        List<NewResponse> newResponses = mappers.toResp(newsList);
                        response.setNewResponses(newResponses);
                        return Single.just(Optional.of(response));
                    } else {
                        return Single.just(Optional.empty());
                    }
                });*/
        }
    }

    @Override
    public List<NewResponse> getNewsByCategory(Integer categoryId) {


        return null;
    }


}
