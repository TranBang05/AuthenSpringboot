package com.example.demo_mhdigital.service.impl;

import com.example.demo_mhdigital.data.request.NewRequest;
import com.example.demo_mhdigital.data.response.CategoryNewResponse;
import com.example.demo_mhdigital.data.response.NewResponse;
import com.example.demo_mhdigital.data.mappers.CategoryNewMapper;
import com.example.demo_mhdigital.data.mappers.NewMapper;
import com.example.demo_mhdigital.repositories.CategoryRepository;
import com.example.demo_mhdigital.repositories.NewRepository;
import com.example.demo_mhdigital.service.NewService;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.CategoryNew;
import org.com.app.tables.pojos.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewServiceImpl implements NewService {

    @Autowired
    NewRepository newRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    NewMapper mapper;

    @Autowired
    CategoryNewMapper mappers;

    @Override
    public Single<String> addNew(Integer id, NewRequest newRequest) {
        New news = mapper.toNewPojo(newRequest);
        return newRepository.findbyidss(id)
                .flatMap(newInsert->{
                    if (newInsert.isPresent() && newRequest.getCategoryNewId()==id) {
                        return newRepository.addNew(id, news)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                }).onErrorReturnItem("error");
    }

    @Override
    public Single<String> updateNew(Integer id, NewRequest newRequest) {
        New news = mapper.toNewPojo(newRequest);
        return newRepository.findbyidss(id)
                .flatMap(newInsert->{
                    if (newInsert.isPresent() && newRequest.getCategoryNewId()==id) {
                        return newRepository.update(id, news)
                                .map(x -> "success")
                                .onErrorReturnItem("error");
                    } else {
                        return Single.just("ID does not exist");
                    }
                }).onErrorReturnItem("error");
    }

    @Override
    public Single<List<NewResponse>> getNews() {

        /*
        List<New> list = newRepository.getNews();

        List<NewResponse> responses = mapper.toResp(list);

        return responses;*/

//        return newRepository.getNews()
//                .map(list -> mapper.toResp(list));


    /*    List<CategoryNew> categoryList = (List<CategoryNew>) categoryRepository.getCategory();
        List<CategoryNewResponse> categoryResponses = mappers.toResponses(categoryList);

        List<Integer> categoryIds = categoryResponses.stream()
                .map(CategoryNewResponse::getCategoryNewId)
                .collect(Collectors.toList());

        List<New> newsList = (List<New>) newRepository.findByCategoryIds(categoryIds);
        List<NewResponse> newResponses = mapper.toResp(newsList);



        Map<Integer, List<CategoryNewResponse>> categorymap = categoryResponses.stream()
                .filter(response -> response.getCategoryNewId() != null)
                .collect(Collectors.groupingBy(CategoryNewResponse::getCategoryNewId));



        newResponses.stream()
                .peek(res ->{
                    //dung code tren
                    res.setCategoryNewResponse((CategoryNewResponse) categorymap.getOrDefault(res.getCategoryNewId(), null));
                    //res.setNewResponses(newResponseMap.get(res.getCategoryNewId())); // k dung
                    Optional<CategoryNewResponse> categoryResponse = findById(res.getCategoryNewId());
                    categoryResponse.ifPresent(category -> res.setCategoryNewResponse(category));

                })
                .collect(Collectors.toList());
        return (Single<List<NewResponse>>) newResponses;*/


        return newRepository.getNews()
                .map(list -> {
                    List<NewResponse> responses = mapper.toResp(list);
                    responses.stream()
                            .peek(res ->{

                                Optional<CategoryNewResponse> categoryResponse = findById(res.getCategoryNewId());
                                categoryResponse.ifPresent(res::setCategoryNewResponse);
                            })
                            .collect(Collectors.toList());
                    return responses;
                });

    }



    @Override
    public Optional<CategoryNewResponse> findById(Integer id) {
        Optional<CategoryNew> category = Optional.ofNullable(categoryRepository.findById(id));
        if (category.isPresent()) {
            CategoryNewResponse response = mappers.toResponse(category.get());
            List<New> newsList = newRepository.findByCategoryId(id);
            List<NewResponse> newResponses = mapper.toResp(newsList);
            response.setNewResponses(newResponses);
            return Optional.of(response);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<NewResponse> findByCategoryId(Integer id) {
        List<New> list = newRepository.findByCategoryId(id);
        List<NewResponse> responses=mapper.toResp(list);
        return responses;
    }


}
