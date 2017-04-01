package com.yada.sdk.device.encryption.model;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 加密机域解析
 * Created by fengming on 3/31/17.
 */
public class HsmFieldParser {
    private final Logger logger = LoggerFactory.getLogger(HsmFieldParser.class);

    private final List<IHsmField> fields = new ArrayList<>();


    /**
     * 建立加密机域解析的类
     *
     * @param field 域
     * @return 加密机域解析的类
     */
    private HsmFieldParser build(IHsmField field) {
        fields.add(field);
        return this;
    }

    /**
     * 添加A或者N类型的域
     *
     * @param length 数据长度
     * @return HsmFiledParser
     */
    public HsmFieldParser appendAN(int length) {
        return build(new ANField(length));
    }

    /**
     * 添加一个可变的B类型域
     *
     * @param lenLength 长度域的长度
     * @return HsmFiledParser
     */
    public HsmFieldParser appendLLB(int lenLength) {
        return build(new LLBField(lenLength));
    }

    /**
     * 添加一个尾部B类型域
     *
     * @return HsmFiledParser
     */
    public HsmFieldParser appendLastB() {
        return build(new LastBField());
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
        if (result.size() < 3) {
            logger.warn("parse fields size < 2. rece data is {}.", Hex.encodeHexString(data.array()));
            return null;
        } else if (result.size() == 3) {
            logger.warn("respCode has a error. error code is {}. ", fields.get(2).sValue());
            return null;
        } else if (result.size() == fields.size()) {
            return result;
        } else {
            logger.warn("parse has a unKnow error. rece data is {}. ", Hex.encodeHexString(data.array()));
            return null;
        }
    }
}
