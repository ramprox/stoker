package ru.stoker.database.entity.embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.Product;
import ru.stoker.database.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ProductEstimationId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", foreignKey = @ForeignKey(name = "fk_product_estimating_us_id_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User estOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_product_pr_id_product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    public ProductEstimationId(Long userId, Long productId) {
        User user = new User();
        user.setId(userId);
        this.estOwner = user;
        Product product = new Product();
        product.setId(productId);
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEstimationId other)) return false;
        return Objects.equals(getEstOwner(), other.getEstOwner()) &&
                Objects.equals(getProduct(), other.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEstOwner(), getProduct());
    }

}
