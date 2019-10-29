package com.coinDesk.utils;

/**
 * Created by alekseenkoyuri1989@gmail.com
 * 28.10.2019
 */

@FunctionalInterface
public interface TextPredicate {
    boolean isEmpty(String value);
}
