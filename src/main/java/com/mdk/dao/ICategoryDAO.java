package com.mdk.dao;

import com.mdk.models.Category;
import com.mdk.models.Product;
import com.mdk.paging.Pageble;

import java.util.List;

public interface ICategoryDAO {

    void insert(Category category);
    void edit(Category category);
    void deleteSoft(int id);
    void restore(int id);
    void delete(int id);

    Category findById(int id);
    int count(String state, String keyword);
    List<Category> findAll(Pageble pageble, String state, String keyword);
    List<Category> findAll();


}
