package com.blockwit.bwf.services;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Random;

@Component
public class RandomServiceImpl implements RandomService {

    Random random = new Random();

    @Override
    public String nextString5() {
        byte[] array = new byte[5]; // length is bounded by 7
        random.nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }


}
