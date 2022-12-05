package ru.stoker.database.entity.productproperties.notebook;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Processor {

    private String model;

    private int kernelCount;

    private double frequency;

}
