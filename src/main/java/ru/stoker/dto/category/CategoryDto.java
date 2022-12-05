package ru.stoker.dto.category;

import lombok.*;
import ru.stoker.dto.util.validators.IdNotEqualParentId;
import ru.stoker.dto.util.validgroups.OnCreate;
import ru.stoker.dto.util.validgroups.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@IdNotEqualParentId(groups = OnUpdate.class, message = "{category.id.not.equal.parent.id}")
public class CategoryDto {

    @Null(groups = OnCreate.class, message = "{category.id.null}")
    @NotNull(groups = OnUpdate.class, message = "{category.id.not.null}")
    private Long id;

    @NotBlank(message = "{category.name.not.blank}")
    private String name;

    private Long parentId;

}
