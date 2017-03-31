package com.yada.sdk.device.encryption.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 加密机域解析
 * Created by fengming on 3/31/17.
 */
public class HsmFieldParser {

    private final List<IHsmField> fields = new ArrayList<>();


    /**
     * 建立加密机域解析的类
     *
     * @param field 域
     * @return 加密机域解析的类
     */
    public HsmFieldParser build(IHsmField field) {
        fields.add(field);
        return this;
    }

    /**
     * 解析数据
     *
     * @param data 数据
     * @return 返回结果
     */
    public List<IHsmField> parse(ByteBuffer data) {
        List<IHsmField> result = new ArrayList<>();
        int i = 0;
        for (IHsmField field : fields) {
            field.setValue(data);
            result.add(field);
            if (i == 2) {
                String code = field.sValue();
                if (!"00".equals(code)) {
                    break;
                }
            }
            i++;
        }
        return result;
    }
}
