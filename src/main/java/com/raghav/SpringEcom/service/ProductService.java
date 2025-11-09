package com.raghav.SpringEcom.service;

import com.raghav.SpringEcom.model.Product;
import com.raghav.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductByID(int id) {
        //return productRepo.findById(id).get(); // it's not recommended to use .get()
        return productRepo.findById(id).orElse(new Product(-1));
    }
}
