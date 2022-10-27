package ru.stoker.database.entity.productproperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Processor {

    @NotBlank(message = "Модель не должна быть пустой")
    private String model;

    @Positive(message = "Количество ядер должно быть больше нуля")
    private int kernelCount;

    @Positive(message = "Частота процессора должна быть больше нуля")
    private double frequency;

}
