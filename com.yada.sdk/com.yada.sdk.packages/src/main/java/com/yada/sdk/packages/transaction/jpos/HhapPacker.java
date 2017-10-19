package com.yada.sdk.packages.transaction.jpos;

import org.jpos.iso.ISOException;

/**
 * 总对总包解析
 */
public class HhapPacker extends CustomPacker<JposMessage> {

    public HhapPacker() throws ISOException {
        super(7, "/8583hhap.xml", "hhap");
    }

    @Override
    protected JposMessage generateJposMessage() {
        JposMessage msg = new JposMessage();
        msg.setHeader(new byte[]{0x60, 0x12, 0x34, 0x56, 0x78, 0x10, 0x00});
        return msg;
    }

    @Override
    protected String generateTranId(JposMessage message) {
        return message.getFieldString(11);
    }

}
