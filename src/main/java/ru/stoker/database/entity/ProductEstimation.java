package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.database.entity.embeddable.ProductEstimationId;

import javax.persistence.*;
import javax.validation.Valid;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_estimations")
public class ProductEstimation {

    @Getter
    @Valid
    @EmbeddedId
    private ProductEstimationId productEstimationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimating_user_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_product_esting_us_id_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User estimatingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_product_pr_id_product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Getter
    @Setter
    @Valid
    @Embedded
    private Estimation estimation;

    public ProductEstimation(Long userId, Long productId, Estimation estimation) {
        productEstimationId = new ProductEstimationId(userId, productId);
        this.estimation = estimation;
    }

}
