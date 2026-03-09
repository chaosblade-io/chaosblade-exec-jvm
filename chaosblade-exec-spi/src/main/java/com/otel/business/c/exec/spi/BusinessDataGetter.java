package com.otel.business.c.exec.spi;

/**
 * @author wufunc@gmail.com
 */
public interface BusinessDataGetter {
    String get(String key) throws Exception;
}