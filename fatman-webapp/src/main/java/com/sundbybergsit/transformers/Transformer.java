package com.sundbybergsit.transformers;

/**
 * Created by IntelliJ IDEA.
 * User: Z
 * Date: 2010-apr-17
 * Time: 20:33:40
 * To change this template use File | Settings | File Templates.
 */
public interface Transformer <U,V> {
    V transform(U objectToTransform);
}
