package ru.stoker.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.database.entity.embeddable.ProductEstimationId;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_estimations")
public class ProductEstimation {

    @EmbeddedId
    private ProductEstimationId id;

    @Embedded
    private Estimation estimation;

    public ProductEstimation(ProductEstimationId id, Estimation estimation) {
        this.id = id;
        this.estimation = estimation;
    }

}
