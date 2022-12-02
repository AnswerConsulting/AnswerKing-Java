package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.request.ProductRequest;
import com.answerdigital.answerking.response.ProductResponse;
import com.answerdigital.answerking.service.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", imports = {List.class, CategoryMapper.class, CategoryService.class},
        uses = {CategoryService.class})
public interface ProductMapper {
    @Mapping(target = "retired", constant = "false")
    @Mapping(target = "category", source = "categoryId")
    Product addRequestToProduct(ProductRequest productRequest);

    Product updateRequestToProduct(@MappingTarget Product product, ProductRequest productRequest);

    @Mapping(target = "categories", expression = "java(List.of(product.getCategory().getId()))")
    ProductResponse convertProductEntityToProductResponse(Product product);

}
