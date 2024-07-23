package com.example.demo_mhdigital.controllers;


import com.example.demo_mhdigital.data.request.CategoryNewRequest;
import com.example.demo_mhdigital.data.response.CategoryNewResponse;
import com.example.demo_mhdigital.data.response.NewResponse;
import com.example.demo_mhdigital.service.CategoryService;
import com.example.demo_mhdigital.service.NewService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryNewController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    NewService newService;

    @GetMapping("/getCategory/")
    public Single<ResponseEntity<List<CategoryNewResponse>>> getCategory(){
        return categoryService.getCategory()
                .map((ResponseEntity::ok));

    }

    @PostMapping("/addCategory")
    public Single<ResponseEntity<String>> addCategory(@RequestBody CategoryNewRequest categoryNewRequest) {
        return categoryService.addCategory(categoryNewRequest).map((ResponseEntity::ok));
    }

    @PutMapping("getCategory/{id}")
    public Single<ResponseEntity<String>> updateCategory(@PathVariable("id") Integer id , @RequestBody CategoryNewRequest categoryNewRequest) {

        return categoryService.updateCategory(id, categoryNewRequest).map((ResponseEntity::ok));
    }

    @DeleteMapping("getCategory/{id}")
    public Single<ResponseEntity<String>> updateCategory(@PathVariable("id") Integer id) {

        return categoryService.deleteCategory(id).map((ResponseEntity::ok));
    }


    @GetMapping("/getCategory/{id}")
    public ResponseEntity<CategoryNewResponse> getCategoryid(@PathVariable("id") Integer id){
        Optional<CategoryNewResponse> category = categoryService.findById(id);
        return ResponseEntity.ok(category.get());
    }

    @GetMapping("/getCategory/new/{id}")
    public  List<NewResponse> getNewByCategoryId(@PathVariable Integer id){
        return newService.findByCategoryId(id);
    }

}
