package com.yada.sdk.device.encryption.model;

import java.nio.ByteBuffer;

/**
 * 定长A或者N类型的加密机所用的域
 * Created by fengming on 3/31/17.
 */
public class ANField implements IHsmField {

    // 储存数据
    private final byte[] data;

    /**
     * 通过域长度来创建一个A或者N类型域
     *
     * @param length 长度
     */
    public ANField(int length) {
        this(new byte[length]);
    }

    /**
     * 通过字符串数据来创建A或者N类型的域
     *
     * @param sData 字符串数据
     */
    ANField(String sData) {
        this(sData.getBytes());
    }

    /**
     * 根据数据创建一个A或者N类型的域
     *
     * @param data 数据
     */
    private ANField(byte[] data) {
        this.data = data;
    }

    @Override
    public int length() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec length");
        }
        return data.length;
    }

    @Override
    public String sValue() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec sValue");
        }
        return new String(data);
    }

    @Override
    public byte[] value() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec value");
        }
        return data;
    }

    @Override
    public String type() {
        return "AN";
    }

    @Override
    public void setValue(ByteBuffer data) {
        data.get(this.data);
    }
}
