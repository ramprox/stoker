package ru.stoker.service.productestimation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.ProductEstimation;
import ru.stoker.database.entity.embeddable.ProductEstimationId;
import ru.stoker.database.repository.ProductEstimationRepository;
import ru.stoker.database.repository.ProductRepository;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;
import ru.stoker.exceptions.ProductEx.NotFoundException;
import ru.stoker.exceptions.ProductEstimationEx;
import ru.stoker.exceptions.ProductEstimationEx.AlreadyExistException;
import ru.stoker.exceptions.ProductEstimationEx.UserSelfEstimationException;
import ru.stoker.mapper.ProductEstimationMapper;

import java.util.List;

@Service
public class ProductEstimationServiceImpl implements ProductEstimationService {

    private final ProductEstimationRepository productEstimationRepository;

    private final ProductRepository productRepository;

    private final ProductEstimationMapper productEstimationMapper;

    @Autowired
    public ProductEstimationServiceImpl(ProductEstimationRepository productEstimationRepository,
                                        ProductRepository productRepository,
                                        ProductEstimationMapper productEstimationMapper) {
        this.productEstimationRepository = productEstimationRepository;
        this.productRepository = productRepository;
        this.productEstimationMapper = productEstimationMapper;
    }

    @Override
    @Transactional
    public ProductEstimationInfo estimate(Long userId, ProductEstimationDto estimation) {
        Long productId = estimation.getProductId();
        checkUserNotEstimateSelfProduct(userId, productId);
        checkNotExistProductEstimation(userId, productId);
        ProductEstimation productEstimation =
                productEstimationMapper.toProductEstimation(estimation, userId);
        productEstimationRepository.save(productEstimation);
        return productEstimationMapper.fromProductEstimation(productEstimation);
    }

    @Override
    public ProductEstimationInfo getById(Long userId, Long id) {
        ProductEstimation productEstimation = getProductEstimationById(userId, id);
        return productEstimationMapper.fromProductEstimation(productEstimation);
    }

    @Override
    public List<ProductEstimationInfo> getAll(Long productId) {
        List<ProductEstimation> estimations = productEstimationRepository.findByIdProductId(productId);
        return productEstimationMapper.fromListProductEstimation(estimations);
    }

    @Override
    @Transactional
    public ProductEstimationInfo update(ProductEstimationDto estimationDto, Long userId) {
        Long productId = estimationDto.getProductId();
        ProductEstimation productEstimation = getProductEstimationById(userId, productId);
        productEstimationMapper.updateFromProductEstimationDto(estimationDto, productEstimation);
        return productEstimationMapper.fromProductEstimation(productEstimation);
    }

    @Override
    @Transactional
    public void deleteByProductId(Long userId, Long productId) {
        ProductEstimation estimation = getProductEstimationById(userId, productId);
        productEstimationRepository.delete(estimation);
    }

    private void checkUserNotEstimateSelfProduct(Long userId, Long productId) {
        Long productOwnerId = productRepository.findProductOwnerIdByProductId(productId)
                .orElseThrow(() -> new NotFoundException(productId));
        if(productOwnerId.equals(userId)) {
            throw new UserSelfEstimationException(userId, productId);
        }
    }

    private void checkNotExistProductEstimation(Long userId, Long productId) {
        ProductEstimationId peId = new ProductEstimationId(userId, productId);
        if(productEstimationRepository.existsById(peId)) {
            throw new AlreadyExistException(userId, productId);
        }
    }

    private ProductEstimation getProductEstimationById(Long userId, Long productId) {
        ProductEstimationId estimationId = new ProductEstimationId(userId, productId);
        return productEstimationRepository.findById(estimationId)
                .orElseThrow(() -> new ProductEstimationEx.NotFoundException(userId, productId));
    }

}
