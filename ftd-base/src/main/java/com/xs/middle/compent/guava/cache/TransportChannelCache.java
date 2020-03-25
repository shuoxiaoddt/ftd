package com.xs.middle.compent.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.graph.Graph;

/**
 * @author xiaos
 * @date 2019/11/15 10:19
 */
public class TransportChannelCache {

    public static void main(String[] args) {
        CacheCompent<String> cacheCompent = CacheCompent.create(100);
        cacheCompent.get("s",() -> {
            return "ds";
        });

    }
}
