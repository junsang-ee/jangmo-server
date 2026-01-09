package com.jangmo.web.model.dto.response.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoPlaceSearchResponse {

	private Meta meta;
	private List<Document> documents;

	@Getter
	@NoArgsConstructor
	public static class Meta {

		@JsonProperty("total_count")
		private int totalCount;
		@JsonProperty("pageable_count")
		private int pageableCount;

		@JsonProperty("is_end")
		private boolean isEnd;

		@JsonProperty("same_name")
		private SameName sameName;
	}

	@Getter
	@NoArgsConstructor
	public static class SameName {

		private List<String> region;
		private String keyword;

		@JsonProperty("selected_region")
		private String selectedRegion;
	}

	@Getter
	@NoArgsConstructor
	public static class Document {

		@JsonProperty("place_name")
		private String placeName;

		private String distance;

		@JsonProperty("place_url")
		private String placeUrl;

		@JsonProperty("category_name")
		private String categoryName;

		@JsonProperty("address_name")
		private String addressName;

		@JsonProperty("road_address_name")
		private String roadAddressName;

		@JsonProperty("id")
		private String placeId;

		private String phone;

		@JsonProperty("category_group_code")
		private String categoryGroupCode;

		@JsonProperty("category_group_name")
		private String categoryGroupName;

		@JsonProperty("x")
		private Double longitude;

		@JsonProperty("y")
		private Double latitude;
	}
}
