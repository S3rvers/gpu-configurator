package com.amalitech.gpuconfigurator.service.featured;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeaturedServiceImpl implements FeaturedService {
    private final ProductRepository productRepository;
    @Override
    public List<Product> getAllFeaturedProduct() {
        return productRepository.getFeaturedProduct().orElse(Collections.emptyList());
    }

    @Override
    public FeaturedResponseDto addFeaturedProduct(UUID id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException("Product does not exist")
            );

            if(Boolean.TRUE.equals(product.getFeatured())){
                return FeaturedResponseDto.builder().message("Product is already Featured").build();
            }
            product.setFeatured(true);
            productRepository.save(product);
            return FeaturedResponseDto.builder().message("Now a featured Product").build();

        }catch (NotFoundException e){
            throw new NotFoundException("Product does not exist");
        }
    }

    @Override
    public FeaturedResponseDto removeFeaturedProduct(UUID id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(
                    ()-> new NotFoundException("Product does not exist")
            );

            if(Boolean.FALSE.equals(product.getFeatured())){
                return FeaturedResponseDto.builder().message("Product is not featured").build();
            }
            product.setFeatured(false);
            productRepository.save(product);
            return FeaturedResponseDto.builder().message("Product is no more featured").build();

        }catch (NotFoundException e){
            throw new NotFoundException("Product does not exist");
        }
    }

}
