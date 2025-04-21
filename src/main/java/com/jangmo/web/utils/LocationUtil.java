package com.jangmo.web.utils;

public class LocationUtil {

    public static String getStandardCityName(String cityName) {
        switch (cityName) {
            case "서울시":
            case "서울":
                return "서울특별시";
            case "경기":
                return "경기도";
            case "강원":
                return "강원도";
            case "충북":
                return "충청북도";
            case "충남":
                return "충청남도";
            case "전북":
                return "전라북도";
            case "전남":
                return "전라남도";
            case "경북":
                return "경상북도";
            case "경남":
                return "경상남도";
            case "세종":
            case "세종시":
                return "세종특별자치시";
            case "인천":
            case "인천시":
                return "인천광역시";
            case "대구":
            case "대구시":
                return "대구광역시";
            case "부산":
            case "부산시":
                return "부산광역시";
            case "대전":
            case "대전시":
                return "대전광역시";
            case "울산":
            case "울산시":
                return "울산광역시";
            case "광주":
            case "광주시":
                return "광주광역시";
            case "제주" :
            case "제주도":
                return "제주특별자치도";
            default: return cityName;
        }
    }
}
