package training.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import training.ecommerce.model.Product;
import training.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieve all products.
     *
     * @return a list of all available products
     */
    public List<Product> index() {
        return productRepository.findAll();
    }
}