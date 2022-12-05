package ru.stoker.database.entity.embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Estimation {

    @Column(name = "value", nullable = false)
    private int value;

    @Column(name = "comment", length = 1024)
    private String comment;

    @Column(name = "estimated_at", nullable = false)
    private LocalDateTime estimatedAt;

    public Estimation(int value, LocalDateTime estimatedAt) {
        this.value = value;
        this.estimatedAt = estimatedAt;
    }

}
