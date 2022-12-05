package ru.stoker.dto.advt;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.stoker.dto.product.ProductInfo;

import java.time.LocalDate;

@Data
public class AdvtInfo {

    private Long id;

    private String name;

    private ProductInfo product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postedAt;

    private Long userId;

}
