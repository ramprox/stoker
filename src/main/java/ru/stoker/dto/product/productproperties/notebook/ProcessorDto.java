package ru.stoker.dto.product.productproperties.notebook;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class ProcessorDto {

    @NotBlank(message = "{processor.model.not.blank}")
    private String model;

    @Positive(message = "{processor.kernelCount.positive}")
    private int kernelCount;

    @Positive(message = "{processor.frequency.positive}")
    private double frequency;

}
