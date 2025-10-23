package com.jangmo.web.model.entity.board;

import com.jangmo.web.constants.BoardActivationStatus;
import com.jangmo.web.model.ModificationTimestampEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "board")
public class BoardEntity extends ModificationTimestampEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BoardActivationStatus status;

    @OneToMany(mappedBy = "parentBoard",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<PostEntity> posts;

    private BoardEntity(String name) {
        this.name = name;
        this.status = BoardActivationStatus.ENABLED;
        this.posts = new ArrayList<>();
    }

    public static BoardEntity create(final String name) {
        return new BoardEntity(name);
    }

    public void update(String name) {
        this.name = name;
    }
}
