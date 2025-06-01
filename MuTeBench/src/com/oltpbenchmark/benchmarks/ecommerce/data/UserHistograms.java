package com.oltpbenchmark.benchmarks.ecommerce.data;

import com.oltpbenchmark.util.Histogram;

public abstract class UserHistograms {

    /**
     * The length of the USER_NAME column
     */
    public static final Histogram<Integer> NAME_LENGTH = new Histogram<>();

    static {
        NAME_LENGTH.put(1, 29);
        NAME_LENGTH.put(2, 151);
        NAME_LENGTH.put(3, 1278);
        NAME_LENGTH.put(4, 2568);
        NAME_LENGTH.put(5, 5384);
        NAME_LENGTH.put(6, 9895);
        NAME_LENGTH.put(7, 12203);
        NAME_LENGTH.put(8, 13861);
        NAME_LENGTH.put(9, 11462);
        NAME_LENGTH.put(10, 10576);
        NAME_LENGTH.put(11, 8101);
        NAME_LENGTH.put(12, 6671);
        NAME_LENGTH.put(13, 4948);
        NAME_LENGTH.put(14, 3770);
        NAME_LENGTH.put(15, 2642);
        NAME_LENGTH.put(16, 1755);
        NAME_LENGTH.put(17, 1009);
        NAME_LENGTH.put(18, 691);
        NAME_LENGTH.put(19, 468);
        NAME_LENGTH.put(20, 372);
        NAME_LENGTH.put(21, 260);
        NAME_LENGTH.put(22, 185);
        NAME_LENGTH.put(23, 107);
        NAME_LENGTH.put(24, 75);
        NAME_LENGTH.put(25, 53);
        NAME_LENGTH.put(26, 52);
        NAME_LENGTH.put(27, 39);
        NAME_LENGTH.put(28, 29);
        NAME_LENGTH.put(29, 27);
        NAME_LENGTH.put(30, 27);
        NAME_LENGTH.put(31, 14);
        NAME_LENGTH.put(32, 12);
        NAME_LENGTH.put(33, 10);
        NAME_LENGTH.put(34, 9);
        NAME_LENGTH.put(35, 7);
        NAME_LENGTH.put(36, 6);
        NAME_LENGTH.put(37, 8);
        NAME_LENGTH.put(38, 6);
        NAME_LENGTH.put(39, 6);
        NAME_LENGTH.put(40, 4);
        NAME_LENGTH.put(41, 1);
        NAME_LENGTH.put(42, 2);
        NAME_LENGTH.put(44, 2);
        NAME_LENGTH.put(45, 1);
        NAME_LENGTH.put(47, 1);
        NAME_LENGTH.put(49, 1);
        NAME_LENGTH.put(50, 1);
        NAME_LENGTH.put(53, 3);
        NAME_LENGTH.put(54, 1);
        NAME_LENGTH.put(56, 2);
        NAME_LENGTH.put(60, 1);
        NAME_LENGTH.put(62, 1);
        NAME_LENGTH.put(63, 1);
    }

    /**
     * The length of the USER_REAL_NAME column
     */
    public static final Histogram<Integer> REAL_NAME_LENGTH = new Histogram<>();

    static {
        REAL_NAME_LENGTH.putAll();
    }

    /**
     * The histogram of the number of users per revision updates
     */
    public static final Histogram<Integer> REVISION_COUNT = new Histogram<>();

    static {
        REVISION_COUNT.put(1, 41764);
        REVISION_COUNT.put(2, 16092);
        REVISION_COUNT.put(3, 8401);
        REVISION_COUNT.put(4, 5159);
        REVISION_COUNT.put(5, 3562);
        REVISION_COUNT.put(6, 2673);
        REVISION_COUNT.put(7, 2115);
        REVISION_COUNT.put(8, 1514);
        REVISION_COUNT.put(9, 1388);
        REVISION_COUNT.put(10, 1144);
        REVISION_COUNT.put(11, 961);
        REVISION_COUNT.put(12, 830);
        REVISION_COUNT.put(13, 728);
        REVISION_COUNT.put(14, 668);
        REVISION_COUNT.put(15, 547);
        REVISION_COUNT.put(16, 516);
        REVISION_COUNT.put(17, 477);
        REVISION_COUNT.put(18, 427);
        REVISION_COUNT.put(19, 392);
        REVISION_COUNT.put(20, 364);
    }
}
