package ru.stoker.service.advertisement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.repository.AdvertisementRepository;
import ru.stoker.database.repository.CategoryRepository;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.advt.UpdateAdvt;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.Category;
import ru.stoker.mapper.AdvtMapper;
import ru.stoker.service.product.ProductService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final ProductService productService;

    private final AdvtMapper advtMapper;

    private final CategoryRepository categoryRepository;

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
    public AdvtInfo save(Long ownerId, CreateAdvt advtDto) {
        checkCategoryExist(advtDto.getProduct().getCategoryId());
        Advertisement advt = advtMapper.fromCreateAdvt(ownerId, advtDto);
        advertisementRepository.save(advt);
        return advtMapper.toAdvtInfo(advt);
    }

    @Override
    @Transactional
    public AdvtInfo update(Long ownerId, UpdateAdvt advtDto) {
        checkCategoryExist(advtDto.getProduct().getCategoryId());
        Advertisement advt = getAdvtById(advtDto.getId());
        advtMapper.updateFromUpdateAdvt(ownerId, advtDto, advt);
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

    private void checkCategoryExist(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new Category.NotFoundException(categoryId);
        }
    }

}
