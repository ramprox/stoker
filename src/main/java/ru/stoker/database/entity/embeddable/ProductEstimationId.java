package ru.stoker.database.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class ProductEstimationId implements Serializable {

    @NotNull(message = "Id оценивающего пользователя не должен быть пустым")
    @Column(name = "estimating_user_id")
    private Long userId;

    @NotNull(message = "Id оцениваемого продукта не должен быть пустым")
    @Column(name = "product_id")
    private Long productId;

}
