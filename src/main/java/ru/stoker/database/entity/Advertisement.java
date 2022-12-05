package ru.stoker.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = Advertisement.TABLE_NAME)
public class Advertisement {

    public static final String TABLE_NAME = "advertisements";
    public static final String SEQUENCE_NAME = "ads_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advt_seq_gen")
    @SequenceGenerator(name = "advt_seq_gen", sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "posted_at", nullable = false)
    private LocalDate postedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_ads_user_id"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(mappedBy = "advertisement", cascade = { PERSIST, MERGE, REMOVE })
    private Product product;

    public Advertisement(String name, LocalDate postedAt, User user) {
        this.name = name;
        this.postedAt = postedAt;
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.setAdvertisement(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Advertisement other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
