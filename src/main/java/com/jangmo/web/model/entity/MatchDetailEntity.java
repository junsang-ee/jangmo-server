package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match_detail")
public class MatchDetailEntity extends CreationTimestampEntity {

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private GroundEntity ground;

    @OneToMany(mappedBy = "match", cascade = CascadeType.REMOVE)
    private List<MatchUserEntity> players;

}
