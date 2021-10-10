package com.deliveroo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CronTest {
    @org.junit.jupiter.api.Test
    void badCronString() {
        // 5 terms instead of 6
        Cron cron = new Cron("*/15 0 1,15 1-5 /usr/bin/find");
        assertNull(cron.getMinutes());
        assertNull(cron.getHours());
        assertNull(cron.getDaysOfMonth());
        assertNull(cron.getMonths());
        assertNull(cron.getDaysOfWeek());
        assertNull(cron.getCronDetails());
    }

    @org.junit.jupiter.api.Test
    void getMinutes() {
        Cron cron = new Cron("*/15 0 1,15 * 1-5 /usr/bin/find");
        assertEquals("0 15 30 45", Cron.toString("", cron.getMinutes()));
    }

    @org.junit.jupiter.api.Test
    void getHours() {
        Cron cron = new Cron("*/15 0-3,8,11,18-21 1,15 * 1-5 /usr/bin/find");
        assertEquals("0 1 2 3 8 11 18 19 20 21", Cron.toString("", cron.getHours()));
    }

    @org.junit.jupiter.api.Test
    void getDaysOfMonth() {
        Cron cron = new Cron("*/15 0 1,15 * 1-5 /usr/bin/find");
        assertEquals("1 15", Cron.toString("", cron.getDaysOfMonth()));
    }

    @org.junit.jupiter.api.Test
    void getDaysOfMonth_Period() {
        String cronString = "*/15 0 */10 * 1-5 /usr/bin/find";
        Cron cron = new Cron(cronString);
        assertEquals("1 11 21 31", Cron.toString("", cron.getDaysOfMonth()));
    }

    @org.junit.jupiter.api.Test
    void getMonths() {
        String cronString = "*/15 0 1,15 * 1-5 /usr/bin/find";
        Cron cron = new Cron(cronString);
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12", Cron.toString("", cron.getMonths()));
    }

    @org.junit.jupiter.api.Test
    void getDaysOfWeek() {
        Cron cron = new Cron("*/15 0 1,15 * 1-5 /usr/bin/find");
        assertEquals("1 2 3 4 5", Cron.toString("", cron.getDaysOfWeek()));
    }

    @org.junit.jupiter.api.Test
    void getCommnd() {
        Cron cron = new Cron("*/15 0 1,15 * 1-5 /usr/bin/find");
        assertEquals("/usr/bin/find", cron.getCommnd());
    }

    @org.junit.jupiter.api.Test
    void getCronString() {
        Cron cron = new Cron("   */15   0 1,15  *    1-5   /usr/bin/find    ");
        assertEquals("*/15 0 1,15 * 1-5 /usr/bin/find", cron.getCronString());
    }

    @org.junit.jupiter.api.Test
    void getCronDetails() {
        Cron cron = new Cron("*/15 0 1,15 * 1-5 /usr/bin/find");
        String expected = "minute        0 15 30 45\n"
                + "hour          0\n"
                + "day of month  1 15\n"
                + "month         1 2 3 4 5 6 7 8 9 10 11 12\n"
                + "day of week   1 2 3 4 5\n"
                + "command       /usr/bin/find\n";

        assertEquals(expected, cron.getCronDetails());
    }

    @org.junit.jupiter.api.Test
    void genericSelectorMinutes() {
        assertEquals(Collections.emptyList(),
                Cron.genericSelector("", Cron.MINUTES, null));

        assertEquals(Collections.emptyList(),
                Cron.genericSelector("75-80", Cron.MINUTES, null));

        assertEquals(Arrays.asList("31","41"),
                Cron.genericSelector("31,41", Cron.MINUTES, null));

        assertEquals(Arrays.asList("40","41","42","43","44","45"),
                Cron.genericSelector("40-45", Cron.MINUTES, null));

        assertEquals(Collections.emptyList(),
                Cron.genericSelector("*/65", Cron.MINUTES, null));

        assertEquals(Collections.emptyList(),
                Cron.genericSelector("*/AB", Cron.MINUTES, null));

        assertEquals(Arrays.asList("0","25","50"),
                Cron.genericSelector("*/25", Cron.MINUTES, null));

        assertEquals(Arrays.asList("0","25","31","40","41","42","43","44","45","50"),
                Cron.genericSelector("31,41,40-45,*/25", Cron.MINUTES, null));
    }

    @org.junit.jupiter.api.Test
    void genericSelectorHours() {
        assertEquals(Collections.emptyList(),
                Cron.genericSelector("", Cron.HOURS, null));

        assertEquals(Collections.emptyList(),
                Cron.genericSelector("75-80", Cron.HOURS, null));

        assertEquals(Arrays.asList("5", "6"),
                Cron.genericSelector("5,6", Cron.HOURS, null));

        assertEquals(Arrays.asList("10", "11", "12", "13", "14", "15"),
                Cron.genericSelector("10-15", Cron.HOURS, null));

        assertEquals(Arrays.asList("0","6","12","18"),
                Cron.genericSelector("*/6", Cron.HOURS, null));

        assertEquals(Arrays.asList("0","3","4","6","7","8","9","10","16"),
                Cron.genericSelector("3,4,6-10,*/8", Cron.HOURS, null));

        assertEquals(Arrays.asList("0","5","10","11","15","20","23"),
                Cron.genericSelector("*/5,11,23", Cron.HOURS, null));
    }

    @org.junit.jupiter.api.Test
    void genericSelectorDaysOfMonth() {
        assertEquals(Collections.emptyList(),
                Cron.genericSelector("", Cron.DAYS_OF_MONTH, null));

        assertEquals(Collections.emptyList(),
                Cron.genericSelector("35-40", Cron.DAYS_OF_MONTH, null));

        assertEquals(Arrays.asList("5", "6"),
                Cron.genericSelector("5,6", Cron.DAYS_OF_MONTH, null));

        assertEquals(Arrays.asList("10", "11", "12", "13", "14", "15"),
                Cron.genericSelector("10-15", Cron.DAYS_OF_MONTH, null));

        assertEquals(Arrays.asList("1","7","13","19","25","31"),
                Cron.genericSelector("*/6", Cron.DAYS_OF_MONTH, null));

        assertEquals(Arrays.asList("1","3","4","6","7","8","9","10","17","25"),
                Cron.genericSelector("3,4,6-10,*/8", Cron.DAYS_OF_MONTH, null));

        assertEquals(Arrays.asList("1","3","5","11","21","25","26","27","28","29","30","31"),
                Cron.genericSelector("*/10,5,3,25-30", Cron.DAYS_OF_MONTH, null));

    }

    @org.junit.jupiter.api.Test
    void genericSelectorMonths() {
        assertEquals(Collections.emptyList(),
                Cron.genericSelector("", Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));

        assertEquals(Collections.emptyList(),
                Cron.genericSelector("35-40", Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));

        assertEquals(Arrays.asList("JAN","MAR","5", "6","DEC"),
                Cron.genericSelector("DEC,5,6,JAN,MAR", Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));

        assertEquals(Arrays.asList("MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV"),
                Cron.genericSelector("MAR-NOV", Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));

        assertEquals(Arrays.asList("2","3","4","5"),
                Cron.genericSelector("2-5", Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));

        assertEquals(Arrays.asList("1","3","5","7","9","11"),
                Cron.genericSelector("*/2", Cron.MONTH_NUMBERS, null));

        assertEquals(Arrays.asList("1","3","APR","6","7","8","9","10"),
                Cron.genericSelector("3,APR,6-10,*/8", Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        assertEquals("Header A B C D", Cron.toString("Header ", Arrays.asList("A", "B", "C", "D")));
    }

    @org.junit.jupiter.api.Test
    void hashSort() {
        List<String> input = Arrays.asList("11", "SEP", "JAN", "6", "DEC", "AUG", "2", "MAY", "7", "APR", "OCT", "MAR");
        assertEquals(Arrays.asList("JAN", "2", "MAR", "APR", "MAY", "6", "7", "AUG", "SEP", "OCT", "11", "DEC"),
                Cron.hashSort(input, Cron.MONTH_NUMBERS, Cron.MONTH_NAMES));
    }
}