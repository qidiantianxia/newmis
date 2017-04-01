package com.yada.sdk.device.encryption.model;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

/**
 * 变长B类型域
 * Created by fengming on 3/31/17.
 */
public class LLBField implements IHsmField {

    private byte[] head;
    private byte[] body = null;
    private byte[] fullData = null;

    LLBField(int headLength) {
        this.head = new byte[headLength];
    }

    LLBField(int headLength, byte[] body) {
        setBody(headLength, body);
    }

    @Override
    public int length() {
        if (body == null) {
            throw new RuntimeException("must exec setValue before exec length");
        }
        return body.length;
    }

    @Override
    public String sValue() {
        if (body == null) {
            throw new RuntimeException("must exec setValue before exec sValue");
        }
        return Hex.encodeHexString(body);
    }

    @Override
    public byte[] value() {
        if (body == null) {
            throw new RuntimeException("must exec setValue before exec value");
        }
        return this.body;
    }

    @Override
    public byte[] fullValue() {
        return fullData;
    }

    @Override
    public int fullLength() {
        return fullData.length;
    }

    @Override
    public String type() {
        return "LLB";
    }

    @Override
    public void setValue(ByteBuffer data) {
        data.get(head);
        int dataLength = Integer.valueOf(new String(head));
        this.body = new byte[dataLength];
        data.get(body);
        setBody(head.length, body);
    }

    private void setBody(int headLength, byte[] body) {
        this.body = body;
        final String length = String.valueOf(body.length);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headLength - length.length(); i++) {
            sb.append("0");
        }
        sb.append(length);
        this.head = sb.toString().getBytes();
        final ByteBuffer allocate = ByteBuffer.allocate(this.head.length + this.body.length);
        allocate.put(this.head);
        allocate.put(this.body);
        this.fullData = allocate.array();
    }
}
