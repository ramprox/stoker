package ru.stoker.service.advertisement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.entity.Product;
import ru.stoker.database.repository.AdvertisementRepository;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.advt.UpdateAdvt;
import ru.stoker.exceptions.Advt;
import ru.stoker.mapper.AdvtMapper;
import ru.stoker.service.product.ProductService;

import java.util.List;

@Slf4j
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final ProductService productService;

    private final AdvtMapper advtMapper;

    @Autowired
    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository,
                                    ProductService productService,
                                    AdvtMapper advtMapper) {
        this.advertisementRepository = advertisementRepository;
        this.productService = productService;
        this.advtMapper = advtMapper;
    }

    @Override
    @Transactional
    public AdvtInfo getById(Long id) {
        Advertisement advertisement = getAdvtById(id);
        return advtMapper.toAdvtInfo(advertisement);
    }

    @Override
    @Transactional
    public List<AdvtInfo> findAll(Long categoryId) {
        List<Advertisement> advertisements = advertisementRepository.findByProductCategoryId(categoryId);
        return advtMapper.toListAdvtInfo(advertisements);
    }

    @Override
    @Transactional
    public AdvtInfo save(Long ownerId, CreateAdvt advtDto, List<MultipartFile> files) {
        Advertisement advt = advtMapper.fromCreateAdvt(ownerId, advtDto);
        advertisementRepository.save(advt);
        productService.save(advt.getProduct(), files);
        return advtMapper.toAdvtInfo(advt);
    }

    @Override
    @Transactional
    public AdvtInfo update(Long ownerId, UpdateAdvt advtDto, List<MultipartFile> files) {
        Advertisement advt = getAdvtById(advtDto.getId());
        advtMapper.updateFromUpdateAdvt(ownerId, advtDto, advt);
        Product product = advt.getProduct();
        List<Long> removingAttaches = advtDto.getProduct().getRemovingAttachments();
        productService.update(product, files, removingAttaches);
        return advtMapper.toAdvtInfo(advt);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Advertisement advertisement = getAdvtById(id);
        advertisementRepository.delete(advertisement);
        productService.deleteAll(id);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        List<Advertisement> adverts = advertisementRepository.findByUserId(userId);
        adverts.stream()
                .map(Advertisement::getId)
                .forEach(this::deleteById);
    }

    private Advertisement getAdvtById(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new Advt.NotFoundException(id));
    }

}
