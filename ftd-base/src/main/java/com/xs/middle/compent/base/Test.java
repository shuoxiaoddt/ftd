package com.xs.middle.compent.base;

/**
 * @author xiaos
 * @date 31/03/2020 10:59
 */
public class Test {
    public static void main(String[] args) {
        String key = "ADS0D111111111E";
        System.out.println(key.hashCode());
        System.out.println(key.hashCode() >>> 16);
        System.out.println(Integer.toBinaryString(key.hashCode()));
        //00000000000000001011111000010000

        System.out.println(hash(key));
    }
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
