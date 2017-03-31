package com.yada.sdk.device.encryption.model;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * 定长B类型的数据域
 * Created by fengming on 3/31/17.
 */
public class BField implements IHsmField {

    private final byte[] data;

    /**
     * 通过域长度来创建一个B类型域
     *
     * @param length 长度
     */
    public BField(int length) {
        this(new byte[length]);
    }

    /**
     * 通过字符串数据来创建B类型的域
     *
     * @param sData 字符串数据
     */
    public BField(String sData) {
        this(sData.getBytes());
    }

    /**
     * 根据数据创建一个B类型的域
     *
     * @param data 数据
     */
    private BField(byte[] data) {
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
        return Hex.encodeHexString(data);
    }

    @Override
    public byte[] value() {
        if (data == null) {
            throw new RuntimeException("must exec setValue before exec value");
        }
        return data;
    }

    @Override
    public byte[] fullValue() {
        return value();
    }

    @Override
    public int fullLength() {
        return length();
    }

    @Override
    public String type() {
        return "B";
    }

    @Override
    public void setValue(ByteBuffer data) {
        data.get(this.data);
    }
}
