package ru.stoker.util.factory;

import ru.stoker.dto.advt.*;
import ru.stoker.dto.product.CreateProduct;
import ru.stoker.dto.product.ProductInfo;
import ru.stoker.dto.product.UpdateProduct;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AdvtDtosFactory {

    public static CreateAdvt getCreateAdvt(String name, CreateProduct product) {
        CreateAdvt createAdvt = new CreateAdvt();
        createAdvt.setName(name);
        createAdvt.setProduct(product);
        return createAdvt;
    }

    public static CreateProduct getCreateProduct(Long categoryId,
                                                 BigDecimal price,
                                                 String description,
                                                 ProductPropertiesDto propertiesDto) {
        CreateProduct createProduct = new CreateProduct();
        createProduct.setCategoryId(categoryId);
        createProduct.setPrice(price);
        createProduct.setDescription(description);
        createProduct.setProperties(propertiesDto);
        return createProduct;
    }

    public static ProductInfo createProductInfo(CreateProduct product, ProductPropertiesDto properties,
                                                List<Long> attachments) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setPrice(product.getPrice());
        productInfo.setCategoryId(product.getCategoryId());
        productInfo.setDescription(product.getDescription());
        productInfo.setAttachments(attachments);
        productInfo.setProperties(properties);
        return productInfo;
    }

    public static AdvtInfo createAdvtInfo(Long id, Long userId, String name, ProductInfo productDto,
                                             LocalDate date) {
        AdvtInfo advt = new AdvtInfo();
        advt.setId(id);
        advt.setName(name);
        advt.setProduct(productDto);
        advt.setPostedAt(date);
        advt.setUserId(userId);
        return advt;
    }

    public static UpdateProduct createUpdateProduct(ProductInfo productDto, List<Long> removingAttachments) {
        UpdateProduct updateProduct = new UpdateProduct();
        updateProduct.setPrice(productDto.getPrice());
        updateProduct.setProperties(productDto.getProperties());
        updateProduct.setDescription(productDto.getDescription());
        updateProduct.setCategoryId(productDto.getCategoryId());
        updateProduct.setRemovingAttachments(removingAttachments);
        return updateProduct;
    }

    public static UpdateAdvt createUpdateAdvt(Long id, String name, UpdateProduct product) {
        UpdateAdvt advt = new UpdateAdvt();
        advt.setId(id);
        advt.setName(name);
        advt.setProduct(product);
        return advt;
    }

    public static AdminUpdateAdvt createAdminUpdateAdvt(Long userId, Long id,
                                                        String name, UpdateProduct product) {
        AdminUpdateAdvt advt = new AdminUpdateAdvt();
        advt.setId(id);
        advt.setUserId(userId);
        advt.setName(name);
        advt.setProduct(product);
        return advt;
    }

    public static AdvtInfo fromAdminCreateAdvt(Long id, AdminCreateAdvt advt, List<Long> attachments,
                                               LocalDate date) {
        ProductInfo productInfo = createProductInfo(advt.getProduct(),
                advt.getProduct().getProperties(), attachments);
        return createAdvtInfo(id, advt.getUserId(), advt.getName(), productInfo, date);

    }

}
