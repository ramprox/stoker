package ru.stoker.util.factory;

import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.estimation.EstimationInfo;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;
import ru.stoker.dto.productestimaton.UpdateProductEstimation;

public class ProductEstimationDtosFactory {

    public static ProductEstimationDto productEstimationDto(Long productId, EstimationDto estimationDto) {
        ProductEstimationDto productEstimationDto = new ProductEstimationDto();
        productEstimationDto.setProductId(productId);
        productEstimationDto.setEstimation(estimationDto);
        return productEstimationDto;
    }

    public static ProductEstimationInfo productEstimationInfo(Long productId, Long userId, EstimationInfo estimationDto) {
        ProductEstimationInfo productEstimationDto = new ProductEstimationInfo();
        productEstimationDto.setProductId(productId);
        productEstimationDto.setEstimation(estimationDto);
        productEstimationDto.setUserId(userId);
        return productEstimationDto;
    }

    public static UpdateProductEstimation updateProductEstimation(Long userId, ProductEstimationDto productEstimationDto) {
        UpdateProductEstimation updateProduct = new UpdateProductEstimation();
        updateProduct.setProductId(productEstimationDto.getProductId());
        updateProduct.setEstimation(productEstimationDto.getEstimation());
        updateProduct.setUserId(userId);
        return updateProduct;
    }

}
