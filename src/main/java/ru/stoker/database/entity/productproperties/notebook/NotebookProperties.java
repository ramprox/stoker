package ru.stoker.database.entity.productproperties.notebook;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stoker.database.entity.productproperties.ProductProperties;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class NotebookProperties extends ProductProperties {

    private String manufacturer;

    private int ram;

    private double screenDiagonal;

    private String os;

    private Processor processor;

}
