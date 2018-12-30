package com.cenkc.jpademo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Base64.*;
import java.util.Date;
import java.util.Random;

/**
 * created by cenkc on 12/31/2018
 */
public class DemoUtils {
    private static Random rand = new Random((new Date()).getTime());
    private static Encoder encoder = Base64.getEncoder();

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static byte[] encrypt(String pass) throws IOException {
        byte[] salt = getSalt();
        byte[] encodedPass = encoder.encode(pass.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(salt);
        os.write(encodedPass);
        return os.toByteArray();
    }

    public static byte[] getSalt() {
        byte[] salt = new byte[8];
        rand.nextBytes(salt);
        return encoder.encode(salt);
    }

    public static String decrypt(String encstr) {
        if (encstr.length() > 12) {
            String cipher = encstr.substring(12);
            Decoder decoder = Base64.getDecoder();
            return new String(decoder.decode(cipher));
        }
        return null;
    }

}
