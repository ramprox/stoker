package ru.stoker.service.productestimation;

import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;

import java.util.List;

public interface ProductEstimationService {

    ProductEstimationInfo estimate(Long userId, ProductEstimationDto estimationDto);

    ProductEstimationInfo getById(Long userId, Long id);

    List<ProductEstimationInfo> getAll(Long productId);

    ProductEstimationInfo update(ProductEstimationDto estimationDto, Long userId);

    void deleteByProductId(Long userId, Long productId);

}
