package ru.stoker.dto.product.productproperties.notebook;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stoker.database.entity.productproperties.notebook.Processor;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class NotebookPropertiesDto extends ProductPropertiesDto {

    @NotBlank(message = "{notebook.manufacturer.not.blank}")
    private String manufacturer;

    @Positive(message = "{notebook.ram.positive}")
    private int ram;

    @Positive(message = "{notebook.screen.diagonal.positive}")
    private double screenDiagonal;

    private String os;

    @Valid
    @NotNull(message = "{notebook.processor.not.null}")
    private ProcessorDto processor;

}
