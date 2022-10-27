package ru.stoker.database.entity.productproperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NotebookProperties extends ProductProperties {

    @NotBlank(message = "Производитель не должен быть пустым")
    private String manufacturer;

    @Positive(message = "Оперативная память должна быть больше нуля")
    private int ram;

    @Positive(message = "Диагональ экрана должна быть больше нуля")
    private double screenDiagonal;

    private String os;

    @Valid
    @NotNull(message = "Описание процессора не должно быть пустым")
    private Processor processor;

}
