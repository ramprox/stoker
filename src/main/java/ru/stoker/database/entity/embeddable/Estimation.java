package ru.stoker.database.entity.embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Embeddable
public class Estimation {

    @Min(value = 1, message = "Оценка должна быть больше или равна 1")
    @Max(value = 10, message = "Оценка должна быть меньше или равна 10")
    @Column(name = "estimation", nullable = false)
    private int estimation;

    @Column(name = "comment", length = 1024)
    private String comment;

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Время формирования оценки не должно быть пустым")
    @Past(message = "Время формирования оценки должно быть прошлым временем")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estimated_at", nullable = false)
    private Date estimatedAt;

    public Estimation(int estimation, Date estimatedAt) {
        this.estimation = estimation;
        this.estimatedAt = estimatedAt;
    }

}
