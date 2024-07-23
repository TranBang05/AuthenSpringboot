package com.example.demo_mhdigital.repositories;

import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface RolePermisionRepository {
    Single<List<String>> getPermissions(String permisionCode, List<String> roles);
}
