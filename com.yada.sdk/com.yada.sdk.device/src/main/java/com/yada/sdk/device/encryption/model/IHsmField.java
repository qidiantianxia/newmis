package com.yada.sdk.device.encryption.model;

import java.nio.ByteBuffer;

/**
 * 加密机域对象
 * Created by fengming on 3/31/17.
 */
public interface IHsmField {

    /**
     * 加密机域长度
     *
     * @return 长度
     */
    int length();

    /**
     * 根据域的类型返回域的值
     *
     * @return 值的字符串显示
     */
    String sValue();

    /**
     * 返回域的真实值
     *
     * @return 值的字节数组
     */
    byte[] value();

    /**
     * 域的类型
     *
     * @return 返回类型
     */
    String type();

    /**
     * 设置值
     *
     * @param data 报文
     */
    void setValue(ByteBuffer data);
}
