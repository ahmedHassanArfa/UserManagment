package com.example.usermanagement.util;

import org.apache.commons.text.RandomStringGenerator;

public class PasswordGeneratorUtil {

    public static String generateRandomPassword(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        return pwdGenerator.generate(length);
    }

}
