package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilteringServiceImpl implements FilteringService {

    private final ProductRepository productRepository;
    private final ConfigOptionsFiltering configOptionsFiltering;


    public List<Product> filterProduct(
            String productCase,
            String price,
            String productType,
            String processor,
            String categories,
            String brand) {

        Specification<Product> spec = createSpecification(
                productCase,
                price,
                productType,
                processor,
                categories,
                brand);

        return productRepository.findAll(spec).stream()
                .filter(product -> !"unassigned".equals(product.getCategory().getCategoryName()))
                .toList();
    }

    @NotNull
    private Specification<Product> createSpecification(
            String productCase,
            String price,
            String productType,
            String processor,
            String categories,
            String brand) {

        Specification<Product> spec = Specification.where(null);

        spec = applyProductCaseFilter(spec, productCase);
        spec = applyCategoriesFilter(spec, categories);
        spec = applyPriceFilter(spec, price);
        spec = applyProductTypeFilter(spec, productType);
        spec = applyBrandFilter(spec, brand);
        spec = applyProcessorFilter(spec, processor);

        return spec;
    }

    private Specification<Product> applyProductCaseFilter(Specification<Product> spec, String productCase) {
        if (productCase != null && !productCase.isEmpty()) {
            String[] productCaseNames = productCase.split(",");
            if (productCaseNames.length == 1) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("productCase").get("name"), productCaseNames[0]));
            } else {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(root.get("productCase").get("name").in((Object[]) productCaseNames)));
            }
        }
        return spec;
    }

    private Specification<Product> applyCategoriesFilter(Specification<Product> spec, String categories) {
        if (categories != null && !categories.isEmpty()) {
            String[] productCategory = categories.split(",");
            if (productCategory.length == 1) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("category").get("categoryName"), productCategory[0]));
            } else {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(root.get("category").get("categoryName").in((Object[]) productCategory)));
            }
        }
        return spec;
    }

    private Specification<Product> applyPriceFilter(Specification<Product> spec, String price) {
        if (price != null && !price.isEmpty()) {
            List<Specification<Product>> newSpec = getSpecifications(price);
            spec = spec.and(newSpec.stream().reduce(Specification::or).orElse(null));
        }
        return spec;
    }

    private Specification<Product> applyProductTypeFilter(Specification<Product> spec, String productType) {
        if (productType != null && !productType.isEmpty()) {
            List<UUID> matchingProductIds = configOptionsFiltering.getProductTypes(productType);
            if (!matchingProductIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProductIds));
            } else {
                return null; // Return an empty specification if no matching product types
            }
        }
        return spec;
    }

    private Specification<Product> applyBrandFilter(Specification<Product> spec, String brand) {
        if (brand != null && !brand.isEmpty()) {
            List<UUID> matchingProductIds = configOptionsFiltering.getBrand(brand);
            if (!matchingProductIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProductIds));
            } else {
                return null; // Return an empty specification if no matching brands
            }
        }
        return spec;
    }

    private Specification<Product> applyProcessorFilter(Specification<Product> spec, String processor) {
        if (processor != null && !processor.isEmpty()) {
            List<UUID> matchingProcessorIds = configOptionsFiltering.getProcessor(processor);
            if (!matchingProcessorIds.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) -> root.get("id").in(matchingProcessorIds));
            } else {
                return null; // Return an empty specification if no matching processors
            }
        }
        return spec;
    }

    @NotNull
    private static List<Specification<Product>> getSpecifications(String price) {
        String[] priceList = price.split(",");
        String productPrice = "totalProductPrice";

        List<Specification<Product>> newSpec = new ArrayList<>();

        for (String priceItem : priceList) {
            if (priceItem.startsWith(">")) {
                double minValue = Double.parseDouble(priceItem.substring(1));
                newSpec.add((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(productPrice), minValue)
                );
            } else {
                var ranges = priceItem.split("-");
                newSpec.add((root, query, criteriaBuilder) ->
                        criteriaBuilder.between(root.get(productPrice), Double.parseDouble(ranges[0]), Double.parseDouble(ranges[1]))
                );
            }
        }
        return newSpec;
    }

}
