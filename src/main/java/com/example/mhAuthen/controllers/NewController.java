package com.example.demo_mhdigital.controllers;



import com.example.demo_mhdigital.data.request.NewRequest;
import com.example.demo_mhdigital.data.response.NewResponse;
import com.example.demo_mhdigital.service.NewService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewController {
    @Autowired
    NewService newService;

    @PostMapping("/addNews/{id}")
    public @NonNull Single<ResponseEntity<String>> addNew(@PathVariable("id") Integer id, @RequestBody NewRequest newRequest){

        return newService.addNew(id,newRequest)
                .map((ResponseEntity::ok));
    }


    @PutMapping("getNews/{id}")
    public Single<ResponseEntity<String>> updateNews(@PathVariable("id") Integer id , @RequestBody NewRequest newRequest) {

        return newService.updateNew(id, newRequest).map((ResponseEntity::ok));
    }

    @GetMapping("/getNews")
    public Single<ResponseEntity<List<NewResponse>>> getNew(){
        return newService.getNews()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/getNews/{id}")
    public  List<NewResponse> getNewByCategoryId( @PathVariable("id") Integer id){
        return newService.findByCategoryId(id);
    }


}
