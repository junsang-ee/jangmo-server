package com.jangmo.web.utils;


import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeGeneratorUtil {
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int CODE_LENGTH = 10;

    public static String getMercenaryCode() {
        Random random = new SecureRandom();
        return IntStream.range(0, CODE_LENGTH)
                .mapToObj(
                        code -> String.valueOf(
                                CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))
                        )
                ).collect(Collectors.joining());

    }
}