package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ground", nullable = false)
    private GroundEntity ground;

    @OneToMany(mappedBy = "match", cascade = CascadeType.REMOVE)
    private List<MatchUserEntity> players;

}
