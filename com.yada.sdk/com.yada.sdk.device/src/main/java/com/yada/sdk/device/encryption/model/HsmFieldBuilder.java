package com.yada.sdk.device.encryption.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 加密机域建立
 * Created by fengming on 3/31/17.
 */
public class HsmFieldBuilder {

    private final List<IHsmField> fields = new ArrayList<>();

    public HsmFieldBuilder appendAN(String data) {
        this.fields.add(new ANField(data));
        return this;
    }

    public HsmFieldBuilder appendLLB(int headLength, byte[] data) {
        this.fields.add(new LLBField(headLength, data));
        return this;
    }

    public ByteBuffer build() {
        int length = 0;
        for (IHsmField field : fields) {
            length = length + field.fullLength();
        }
        ByteBuffer data = ByteBuffer.allocate(length);
        for (IHsmField field : fields) {
            data.put(field.fullValue());
        }
        data.flip();
        return data;
    }
}
