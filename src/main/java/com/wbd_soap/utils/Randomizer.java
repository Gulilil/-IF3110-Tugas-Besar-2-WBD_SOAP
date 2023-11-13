package com.wbd_soap.utils;

import java.util.Random;

public class Randomizer {
    public String generateRandomWord(int wordLength) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(wordLength);
        for(int i = 0; i < wordLength; i++) {
            char tmp = (char) ('a' + r.nextInt(26));
            sb.append(tmp);
        }
        return sb.toString();
    }
}
