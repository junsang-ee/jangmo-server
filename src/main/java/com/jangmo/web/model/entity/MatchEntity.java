package com.jangmo.web.model.entity;

import com.jangmo.web.constants.match.MatchStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "match")
public class MatchEntity extends CreationUserEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchType type;

//    @OneToMany(mappedBy = "match", cascade = CascadeType.REMOVE)
//    private List<MatchUserEntity> players;

    @OneToOne(mappedBy = "match", fetch = FetchType.LAZY)
    private MatchVoteEntity matchVote;


}

