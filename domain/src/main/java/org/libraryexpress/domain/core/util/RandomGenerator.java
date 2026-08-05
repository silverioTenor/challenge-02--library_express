package org.libraryexpress.domain.core.util;

import java.util.Random;
import java.util.UUID;

public abstract class RandomGenerator {

    public static String ISBN() {
        Random random = new Random();

        int pos1 = random.nextInt(1000);
        int pos2 = random.nextInt(100);
        int pos3 = random.nextInt(100000);
        int pos4 = random.nextInt(100);
        int pos5 = random.nextInt(10);

        return String.format("%03d-%02d-%05d-%02d-%d", pos1, pos2, pos3, pos4, pos5);
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }
}
