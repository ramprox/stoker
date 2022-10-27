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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = Attachment.TABLE_NAME, indexes = @Index(name = "uri_uq_idx", columnList = "uri"))
public class Attachment {

    public static final String TABLE_NAME = "attachments";
    public static final String SEQUENCE_NAME = "att_seq";

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "att_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Продукт не должен быть пустым")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_att_product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @NotBlank(message = "URI изображения не должен быть пустым")
    @Column(name = "uri", length = 4096, nullable = false)
    private String uri;

}
