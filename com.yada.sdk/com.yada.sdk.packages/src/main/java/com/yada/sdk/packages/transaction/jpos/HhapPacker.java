package com.yada.sdk.packages.transaction.jpos;

import org.jpos.iso.ISOException;

/**
 * 总对总包解析
 */
public class HhapPacker extends CustomPacker<JposMessage> {

    public HhapPacker() throws ISOException {
        super(0, "/8583hhap.xml", "hhap");
    }

    @Override
    protected JposMessage generateJposMessage() {
        return new JposMessage();
    }

    @Override
    protected String generateTranId(JposMessage message) {
        return message.getFieldString(11);
    }

}
