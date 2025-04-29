package com.jangmo.web.model.entity;

import com.jangmo.web.constants.GroundType;

import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "ground")
public class GroundEntity extends CreationUserEntity {

    @Column(nullable = false)
    private String placeId;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String addressName;

    @Column(nullable = false)
    private String roadAddressName;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroundType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district", nullable = false)
    private District district;

    private GroundEntity(UserEntity createdBy,
                         String placeId,
                         String placeName,
                         String addressName,
                         String roadAddressName,
                         Double longitude,
                         Double latitude,
                         GroundType type,
                         City city,
                         District district) {
        super(createdBy);
        this.placeId = placeId;
        this.placeName = placeName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.city = city;
        this.district = district;
    }

    public static GroundEntity create(final UserEntity createdBy,
                                      final String placeId,
                                      final String placeName,
                                      final String addressName,
                                      final String roadAddressName,
                                      final Double longitude,
                                      final Double latitude,
                                      final GroundType type,
                                      final City city,
                                      final District district) {
        return new GroundEntity(
                createdBy, placeId,
                placeName, addressName,
                roadAddressName,
                longitude, latitude,
                type, city, district
        );
    }

}
