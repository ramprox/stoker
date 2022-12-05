package ru.stoker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.stoker.database.entity.Product;
import ru.stoker.dto.product.CreateProduct;
import ru.stoker.dto.product.ProductInfo;
import ru.stoker.dto.product.UpdateProduct;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {ProductPropertiesMapper.class},
        injectionStrategy = CONSTRUCTOR)
public interface ProductMapper extends AttachmentToLong {

    @Mapping(target = "category.id", source = "categoryId")
    Product fromCreateProduct(CreateProduct createProduct);

    @Mapping(target = "categoryId", source = "category.id")
    ProductInfo toProductInfo(Product product);

    void updateFromUpdateProduct(UpdateProduct updateProduct, @MappingTarget Product product);

}
