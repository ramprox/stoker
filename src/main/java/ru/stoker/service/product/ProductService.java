package ru.stoker.service.product;

import org.springframework.web.multipart.MultipartFile;
import ru.stoker.database.entity.Product;

import java.util.List;

public interface ProductService {

    void save(Product product, List<MultipartFile> files);

    void update(Product product, List<MultipartFile> files, List<Long> removingAttaches);

    void deleteAll(Long productId);

}
