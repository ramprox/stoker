package ru.stoker.service.advertisement;

import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;

public interface AdminAdvtService {

    AdvtInfo create(AdminCreateAdvt advtDto);

    AdvtInfo update(AdminUpdateAdvt advtDto);

    void deleteById(Long id);

}
