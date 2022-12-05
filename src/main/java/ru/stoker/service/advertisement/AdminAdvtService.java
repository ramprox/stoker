package ru.stoker.service.advertisement;

import org.springframework.web.multipart.MultipartFile;
import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;

import java.util.List;

public interface AdminAdvtService {

    AdvtInfo create(AdminCreateAdvt advtDto, List<MultipartFile> files);

    AdvtInfo update(AdminUpdateAdvt advtDto, List<MultipartFile> files);

    void deleteById(Long id);

}
