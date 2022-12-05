package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.enums.ImageType;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = Attachment.TABLE_NAME)
public class Attachment {

    public static final String TABLE_NAME = "attachments";
    public static final String SEQUENCE_NAME = "att_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "att_seq_gen")
    @SequenceGenerator(name = "att_seq_gen", sequenceName = "att_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_att_product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Column(name = "filename", length = 4096, nullable = false)
    private String filename;

    @Column(name = "content_type", length = 20, nullable = false)
    private ImageType contentType;

    public Attachment(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Attachment attachment)) return false;
        return id != null && id.equals(attachment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
