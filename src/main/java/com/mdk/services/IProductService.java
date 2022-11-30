package com.mdk.services;

import com.mdk.models.Product;
import com.mdk.paging.Pageble;

import java.util.List;

public interface IProductService {
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    Product findOneByName(String name, int storeId);
    List<Product> getTopSeller(int index);
    List<Product> findAll();
    List<Product> findAll(Pageble pageble, int categoryId, int storeId, String searchKey);
    List<Product> findByCategoryId(int categoryId);
    void ban(int id, Boolean state);
    String findOwnerEmailByProductId(int id);
    Product findOneById(int id);
    List<Product> findByStoreId(int storeId);
    int count(int categoryId, int storeId, String searchKey);
    int count(String status);
    List<Product> findAll(Pageble pageble, String status);
    List<Product> findAllByStoreId(int id);
    List<Product> findBySearching(String keyword, int categoryId, int storeId, int rating, double minPrice, double maxPrice);
    List<Product> getTopRating(int index);
    List<Product> findAllProductProhibited();
    List<Product> findAllProductPermitted();
    List<Product> topSeller(int storeId, int top);
}
