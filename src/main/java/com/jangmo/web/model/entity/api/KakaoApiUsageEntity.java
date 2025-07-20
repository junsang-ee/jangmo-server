package com.jangmo.web.model.entity.api;

import com.jangmo.web.model.ModificationTimestampEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "kakao_api_usage")
public class KakaoApiUsageEntity extends ModificationTimestampEntity {

    @OneToOne
    @JoinColumn(name = "api_caller", nullable = false)
    private MemberEntity apiCaller;

    private int usedCount;

    private KakaoApiUsageEntity(MemberEntity apiCaller) {
        this.apiCaller = apiCaller;
        this.usedCount = 0;
    }

    public static KakaoApiUsageEntity create(final MemberEntity apiCaller) {
        return new KakaoApiUsageEntity(apiCaller);
    }

    public void increaseUsedCount() {
        this.usedCount++;
    }

}
