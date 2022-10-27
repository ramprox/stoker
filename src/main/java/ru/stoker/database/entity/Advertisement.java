package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = Advertisement.TABLE_NAME)
public class Advertisement {

    public static final String TABLE_NAME = "advertisements";
    public static final String SEQUENCE_NAME = "ads_seq";

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Long id;

    @NotBlank(message = "Имя объявления не должно быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Дата размещения объвления не должна быть пустой")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_at", nullable = false)
    private Date postedAt;

    @NotNull(message = "Автор объвления не должен быть пустым")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_ads_user_id"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(mappedBy = "advertisement", cascade = { PERSIST, MERGE })
    private Product product;

    public Advertisement(String name, Date postedAt, User user) {
        this.name = name;
        this.postedAt = postedAt;
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.setAdvertisement(this);
    }

}
