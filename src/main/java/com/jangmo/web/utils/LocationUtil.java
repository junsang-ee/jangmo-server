package com.jangmo.web.utils;

public class LocationUtil {

	public static String getStandardCityName(String cityName) {
		return switch (cityName) {
			case "서울시", "서울" -> "서울특별시";
			case "경기" -> "경기도";
			case "강원", "강원특별자치도" -> "강원도";
			case "충북" -> "충청북도";
			case "충남" -> "충청남도";
			case "전북" -> "전라북도";
			case "전남" -> "전라남도";
			case "경북" -> "경상북도";
			case "경남" -> "경상남도";
			case "세종", "세종시" -> "세종특별자치시";
			case "인천", "인천시" -> "인천광역시";
			case "대구", "대구시" -> "대구광역시";
			case "부산", "부산시" -> "부산광역시";
			case "대전", "대전시" -> "대전광역시";
			case "울산", "울산시" -> "울산광역시";
			case "광주", "광주시" -> "광주광역시";
			case "제주", "제주도" -> "제주특별자치도";
			default -> cityName;
		};
	}
}
