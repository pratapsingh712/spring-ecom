package com.raghav.SpringEcom.controller;

import com.raghav.SpringEcom.model.Product;
import com.raghav.SpringEcom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api") // for making common api calls starting with /api/xyz for an instance
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product product = productService.getProductByID(id);

        if(product.getId() >= 0)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("product/{productID}/image")
//    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
//        Product product = productService.getProductByID(productId);
//        if(product.getId() >= 0)
//            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @GetMapping(value = "/product/{productId}/image")
    public ResponseEntity<ByteArrayResource> getImageByProductId(@PathVariable("productId") int productId) {
        Product product = productService.getProductByID(productId);
        if (product == null || product.getImageData() == null || product.getImageData().length == 0) {
            return ResponseEntity.notFound().build();
        }

        // If you store content type (e.g., "image/png") in product, use it. Fallback to jpeg.
        String contentType = MediaType.IMAGE_JPEG_VALUE; // default

        ByteArrayResource resource = new ByteArrayResource(product.getImageData());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + (product.getImageName() != null ? product.getImageName() : "image") + "\"")
                .body(resource);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product savedProduct = null;
        try {
            savedProduct = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product updatedProduct = null;
        try{
            updatedProduct = productService.updateProduct(product, imageFile);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        catch (IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = productService.getProductByID(id);
        if(product!=null){
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("product/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> products = productService.searchProducts(keyword);
        System.out.println("searching with :"+keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
