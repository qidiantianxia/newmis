package com.yada.sdk.device.encryption.hsm;

import com.payneteasy.tlv.HexUtil;
import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class EncryptionMachine implements IEncryption {

    private String lmkZmk;
    private InetSocketAddress endPoint;
    private String messageHead = "-------";


    //报文头长度
    private final int headIndex = messageHead.length() + 4;
    //截取字段一Z类型索引16位密钥
    private final int oneParagraphZIndex = headIndex + 8 * 2;
    //截取字段二Z类型索引
    private final int twoParagraphZIndex = oneParagraphZIndex + 8 * 2;
    //截取字段一X类型索引
    private final int oneParagraphXIndex = headIndex + 1 + 16 * 2;
    //截取字段二X类型索引
    private final int twoParagraphXIndex = oneParagraphXIndex + 1 + 16 * 2;

    public EncryptionMachine(String serverIp, int port, String lmkZmk) {
        this.lmkZmk = lmkZmk;
        this.endPoint = new InetSocketAddress(serverIp, port);
    }

    public EncryptionMachine(String serverIp, int port) {
        this.endPoint = new InetSocketAddress(serverIp, port);
    }


    private String send(String reqMessage) {
        ByteBuffer reqBuffer = ByteBuffer.wrap(reqMessage.getBytes());
        return send(reqBuffer);
    }

    // 用于直接发送字节
    private String send(ByteBuffer reqBuffer) {
        TcpClient client = new TcpClient(endPoint,
                new FixLenPackageSplitterFactory(2, false), 2000);
        try {
            client.open();
            ByteBuffer respBuffer = client.send(reqBuffer);
            return new String(respBuffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (client.isOpen()) {
                client.close();
            }
        }
    }

    // 发送字节
    private byte[] sendBytes(byte[] reqMessage) {
        ByteBuffer reqBuffer = ByteBuffer.wrap(reqMessage);
        TcpClient client = new TcpClient(endPoint,
                new FixLenPackageSplitterFactory(2, false), 2000);
        try {
            client.open();
            ByteBuffer respBuffer = client.send(reqBuffer);
            return respBuffer.array();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (client.isOpen()) {
                client.close();
            }
        }
    }

    @Override
    public String getLmkTmk(String zmkTmk) {
        StringBuilder sb = new StringBuilder();

        // 1.消息头 2.命令代码 3.密钥类型 4.lmkZmk
        sb.append(messageHead).append("A6").append("000").append(lmkZmk);
        // 是否需要增加1A
        if (zmkTmk.length() != 16) {
            sb.append("X");
        }
        // 5.zmkTmk 6.tmk加密方案
        sb.append(zmkTmk).append("X");
        String respMessage = send(sb.toString());
        // 1.消息头长度 2.响应码长度 3.错误代码长度
        int startIndex = messageHead.length() + 2 + 2;
        // 返回密钥是否存在1A
        if (zmkTmk.length() != 16) {
            startIndex = startIndex + 1;
        }
        return respMessage.substring(startIndex,
                startIndex + zmkTmk.length());
    }

    @Override
    public String getLmkTak(String lmkTmk, String tmkTak) {

        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码
        sb.append(messageHead).append("MI");
        // 是否需要增加1A
        if (lmkTmk.length() != 16) {
            sb.append("X");
        }
        sb.append(lmkTmk);
        // 是否需要增加1A
        if (tmkTak.length() != 16) {
            sb.append("X");
        }
        sb.append(tmkTak).append(";XX0");
        String respMessage = send(sb.toString());
        // 1.消息头长度 2.响应码长度 3.错误代码长度
        int startIndex = messageHead.length() + 2 + 2;
        // 返回密钥是否存在1A
        if (tmkTak.length() != 16) {
            startIndex = startIndex + 1;
        }
        return respMessage.substring(startIndex,
                startIndex + tmkTak.length());
    }

    @Override
    public String getLmkTpk(String lmkTmk, String tmkTpk) {

        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码
        sb.append(messageHead).append("FA");
        // 是否需要增加1A
        if (lmkTmk.length() != 16) {
            sb.append("X");
        }
        sb.append(lmkTmk);
        // 是否需要增加1A
        if (tmkTpk.length() != 16) {
            sb.append("X");
        }
        sb.append(tmkTpk).append(";XX0");
        String respMessage = send(sb.toString());
        // 1.消息头长度 2.响应码长度 3.错误代码长度
        int startIndex = messageHead.length() + 2 + 2;
        // 返回密钥是否存在1A
        if (tmkTpk.length() != 16) {
            startIndex = startIndex + 1;
        }
        return respMessage.substring(startIndex,
                startIndex + tmkTpk.length());
    }

    @Override
    public String getTpkPin(String accountNo, String pin, String lmkTpk) {
        String subAccountNo = accountNo.substring(accountNo.length() - 13,
                accountNo.length() - 1);
        StringBuilder sb = new StringBuilder();
        sb.append(messageHead);
        sb.append("BA");
        sb.append(String.format("%-7s", pin).replace(' ', 'F'));
        sb.append(subAccountNo);
        sb.append("");
        sb.append("");
        String respMessage = send(sb.toString());
        int startIndex = messageHead.length() + 2 + 2;
        String lmkPin = respMessage.substring(startIndex);

        sb = new StringBuilder();
        sb.append(messageHead);
        sb.append("JG");
        if (lmkTpk.length() != 16) {
            sb.append("X");
        }
        sb.append(lmkTpk);
        sb.append("01");
        sb.append(subAccountNo);
        sb.append(lmkPin);
        sb.append("");
        sb.append("");
        respMessage = send(sb.toString());
        return respMessage.substring(startIndex, startIndex + 8 + 8);
    }

    @Override
    public ByteBuffer getTakMac(ByteBuffer macData, String lmkTak) {

        StringBuilder sb = new StringBuilder();
        sb.append(messageHead).append("MS").append("0").append("0").append("1")
                .append("0");

        if (lmkTak.length() != 16) {
            sb.append("X").append(lmkTak);
        }
        String len = String.format("%04X", macData.array().length);
        sb.append(len);

        byte[] reqMsg = sb.toString().getBytes();

        ByteBuffer buf = ByteBuffer.allocate(macData.array().length
                + reqMsg.length);
        macData.flip();
        buf.put(reqMsg).put(macData);

        buf.flip();

        String respMessage = send(buf);

        int startIndex = messageHead.length() + 2 + 2;

        String mac = respMessage.substring(startIndex, startIndex + 16);

        byte[] macByte = HexUtil.parseHex(mac);

        return ByteBuffer.wrap(macByte);
    }

    @Override
    public String getLmkPinFromZpkPin(String hsmZpkLmk, String classifiedPin,
                                      String cardNo) {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码
        sb.append(messageHead).append("JE");
        // 是否需要增加1A
        if (hsmZpkLmk.length() == 16) {
            sb.append(hsmZpkLmk);
        } else if (hsmZpkLmk.length() == 32) {
            sb.append("X").append(hsmZpkLmk);
        } else {
            throw new RuntimeException("getLmkPinFromZpkPin()密钥长度错误...");
        }


        if (classifiedPin.length() == 16 && cardNo.length() > 13) {
            sb.append(classifiedPin).append("01").append(cardNo.substring(cardNo.length() - 13, cardNo.length() - 1));
        } else {
            throw new RuntimeException("getLmkPinFromZpkPin()-pin密文/主账号长度错误...");
        }

        String respMessage = send(sb.toString());
        String respCode = respMessage.substring(messageHead.length() + 2, messageHead.length() + 2 + 2);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }

        return respMessage.substring(messageHead.length() + 2 + 2);
    }

    @Override
    public String DecryptPin(String classifiedPin, String cardNo) {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码
        sb.append(messageHead).append("NG");

        // 3.PAN 4.PIN密文
        if (cardNo.length() > 13) {
            sb.append(cardNo.substring(cardNo.length() - 13, cardNo.length() - 1)).append(classifiedPin);
        } else {
            throw new RuntimeException("DecryptPin()-主账号长度错误...");
        }

        String respMessage = send(sb.toString());
        String respCode = respMessage.substring(messageHead.length() + 2, messageHead.length() + 2 + 2);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        //去F

        return respMessage.substring(messageHead.length() + 2 + 2, messageHead.length() + 2 + 2 + 6).replace("F", "");
    }

    @Override
    public String GetLmkZpk(String hsmKeyLmk) {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码
        sb.append(messageHead).append("IA");
        // 是否需要增加1A
        if (hsmKeyLmk.length() == 16) {
            sb.append(hsmKeyLmk);
        } else if (hsmKeyLmk.length() == 32) {
            sb.append("X").append(hsmKeyLmk).append(";XX0");
        } else {
            throw new RuntimeException("GetLmkZpk()密钥长度错误...");
        }

        String respMessage = send(sb.toString());
        String respCode = respMessage.substring(messageHead.length() + 2, messageHead.length() + 2 + 2);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }

        return respMessage.substring(messageHead.length() + 2 + 2);
    }

    @Override
    public String GetLmkZak(String hsmKeyLmk) {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码 3.模式（1位）+key type(3位)
        sb.append(messageHead).append("A0").append("1008");
        // 4.Key scheme LMK 5.ZMK 6.Key scheme ZMK
        if (hsmKeyLmk.length() == 16) {
            sb.append("Z").append(hsmKeyLmk).append("Z");
        } else if (hsmKeyLmk.length() == 32) {
            sb.append("X").append(hsmKeyLmk).append("X");
        } else {
            throw new RuntimeException("GetLmkZak()密钥长度错误...");
        }

//		System.out.println(sb.toString());

        String respMessage = send(sb.toString());

//		System.out.println(respMessage);

        String respCode = respMessage.substring(messageHead.length() + 2, messageHead.length() + 2 + 2);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }

        return respMessage.substring(messageHead.length() + 2 + 2);
    }

    @Override
    public String GetLmkZek(String hsmKeyLmk) {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码 3.模式（1位）+key type(3位)
        sb.append(messageHead).append("A0").append("100A");
        // 4.Key scheme LMK 5.ZMK 6.Key scheme ZMK
        if (hsmKeyLmk.length() == 16) {
            sb.append("Z").append(hsmKeyLmk).append("Z");
        } else if (hsmKeyLmk.length() == 32) {
            sb.append("X").append(hsmKeyLmk).append("X");
        } else {
            throw new RuntimeException("GetLmkZek()密钥长度错误...");
        }

        String respMessage = send(sb.toString());
        String respCode = respMessage.substring(messageHead.length() + 2, messageHead.length() + 2 + 2);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }

        return respMessage.substring(messageHead.length() + 2 + 2);
    }

    @Override
    public String getZakMac(String lmkZak, String macData) {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码 3.模式（1位）+key type(3位)
        sb.append(messageHead).append("MS").append("01");
        // 4.Key scheme LMK 5.ZMK 6.Key scheme ZMK
        if (lmkZak.length() == 16) {
            sb.append("00").append(lmkZak);
        } else if (lmkZak.length() == 32) {
            sb.append("10X").append(lmkZak);
        } else {
            throw new RuntimeException("getZakMac()密钥长度错误...");
        }

        String len = String.format("%04X", macData.getBytes().length);
        sb.append(len).append(macData);

        byte[] reqMsg = sb.toString().getBytes();

        ByteBuffer buf = ByteBuffer.allocate(reqMsg.length);
        buf.put(reqMsg);

        buf.flip();
        String respMessage = send(buf);
        String respCode = respMessage.substring(messageHead.length() + 2, messageHead.length() + 2 + 2);

		/*String len = String.format("%04X", macData.length());
        System.out.println("len="+len);
		sb.append(len).append(macData.toString());
		String respMessage = send(sb.toString());
		String respCode = respMessage.substring(messageHead.length()+2, messageHead.length()+2+2);*/

        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }

        return respMessage.substring(messageHead.length() + 2 + 2);
    }

    @Override
    public String[] getDekKeyArray(String lmkZmk) {
        // 1.消息头 2.命令代码 3.模式（1位）+key type(3位)
        // 4.Key scheme LMK 5.ZMK 6.Key scheme ZMK
        String sb = messageHead + "A0" + "1000" +
                "X" + "X" + lmkZmk + "X";

        String respMessage = send(sb);

        String respCode = respMessage.substring(messageHead.length() + 2, headIndex);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        return unpackX(respMessage, true);
    }


    public String[] getZekKeyArray(String lmkDek) {
        // 1.消息头 2.命令代码 3标志(1位) 4.ZMK
        // 5.分隔符 ;6.Key scheme LMK  7.Key scheme ZMK 8.密钥校验值
        String sb = messageHead + "FI" + "0" + "X" + lmkDek +
                ";" + "X" + "X" + "1";
        String respMessage = send(sb);

        String respCode = respMessage.substring(messageHead.length() + 2, headIndex);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        return unpackX(respMessage, true);
    }

    @Override
    public String[] getTmkKeyArray(String zmkTmk, String lmkZmk) {
        // 1.消息头 2.命令代码 3.密钥类型（3位）
        // 4. ZMK 5. ZMK加密过的密钥 6.Key scheme LMK
        String sb = messageHead + "A6" + "000" +
                "X" + lmkZmk + "X" + zmkTmk + "X";

        String respMessage = send(sb);

        String respCode = respMessage.substring(messageHead.length() + 2, headIndex);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        return unpackX(respMessage, false);
    }

    @Override
    public String getDekTmk(String lmkDek, String lmkTmk) {
        // 1.消息头 2.命令代码 3.密钥类型（3位）
        // 4. ZMK 5. LMK加密过的密钥 6.Key scheme ZMK
        String sb = messageHead + "A8" + "000" +
                "X" + lmkDek + "X" + lmkTmk + "X";

        String respMessage = send(sb);

        String respCode = respMessage.substring(messageHead.length() + 2, headIndex);
        if (!respCode.equals("00")) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        return unpackX(respMessage, false)[0];
    }

    @Override
    public byte[] getByteDataByDecryption(byte[] zekData, String lmkZek) throws IOException {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码 3,消息块编号0 4,加解密类型0 5.算法1 6.密钥类型 0(zek)
        sb.append(messageHead).append("E0").append("0110");
        // 7.ZEK 8,导入数据结构 0  9.导出数据结构 0 10,填充模式1 11,填充字符 FFFF 12. 填充计数类型1
        sb.append("X" + lmkZek).append("001FFFF1");
        //  13 加密数据长度 14加密数据
        String lenStrHex = Integer.toHexString(zekData.length);
        lenStrHex = "000".substring(0, 3 - lenStrHex.length()) + lenStrHex;
        sb.append(lenStrHex.toUpperCase());

        byte[] respMessage = sendBiary(sb.toString(),zekData);

        //发送
        byte[] retCodeByte = new byte[2];
        System.arraycopy(respMessage, 9, retCodeByte, 0, 2);
        String respCode = new String(retCodeByte);
        if (!"00".equals(respCode)) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        // 截取应返回数据的长度
        byte[] lengthCount = new byte[3];
        System.arraycopy(respMessage, 12, lengthCount, 0, 3);
        int count = Integer.parseInt(new String(lengthCount), 16);
        byte[] retByte = new byte[respMessage.length - 15];
        System.arraycopy(respMessage, 15, retByte, 0, count);
        return retByte;
    }

    @Override
    public byte[] getByteDataByEncryption(byte[] data, String lmkZek) throws IOException {
        StringBuilder sb = new StringBuilder();
        // 1.消息头 2.命令代码 3,消息块编号0 4,加解密类型0 5.算法1 6.密钥类型 0(zek)
        sb.append(messageHead).append("E0").append("0010");
        // 7.ZEK 8,导入数据结构 0  9.导出数据结构 0 10,填充模式1 11,填充字符 FFFF 12. 填充计数类型1
        sb.append("X" + lmkZek).append("001FFFF1");
        //  13 加密数据长度 14加密数据
        String lenStrHex = Integer.toHexString(data.length);
        lenStrHex = "000".substring(0, 3 - lenStrHex.length()) + lenStrHex;
        sb.append(lenStrHex.toUpperCase());
        byte[] respMessage = sendBiary(sb.toString(),data);

        //发送
        byte[] retCodeByte = new byte[2];
        System.arraycopy(respMessage, 9, retCodeByte, 0, 2);
        String respCode = new String(retCodeByte);
        if (!"00".equals(respCode)) {
            throw new RuntimeException("加密机返回失败！" + respCode);
        }
        // 截取应返回数据的长度
        byte[] lengthCount = new byte[3];
        System.arraycopy(respMessage, 12, lengthCount, 0, 3);
        int count = Integer.parseInt(new String(lengthCount), 16);
        byte[] retByte = new byte[respMessage.length - 15];
        System.arraycopy(respMessage, 15, retByte, 0, count);
        return retByte;
    }
    /**
     * 解析加密机Z类型(16长度)内容
     *
     * @param data 返回数据
     * @return [第一段数据, 第二段数据(如不包含第二段密钥则为空, kcv]
     */
    public String[] unpackZ(String data, boolean isContainTwoKey) {
        String[] returnoneKey = new String[3];
        returnoneKey[0] = data.substring(headIndex, oneParagraphZIndex);
        if (isContainTwoKey) {
            returnoneKey[1] = data.substring(oneParagraphZIndex, twoParagraphZIndex);
            returnoneKey[2] = data.substring(twoParagraphZIndex, twoParagraphZIndex + 16);
        } else {
            returnoneKey[1] = data.substring(oneParagraphZIndex, oneParagraphZIndex + 16);
        }
        return returnoneKey;
    }

    /**
     * 解析加密机X类型(32长度)内容
     *
     * @param data 返回数据
     * @return [第一段数据, 第二段数据(如不包含第二段密钥则为空, kcv]
     */
    public String[] unpackX(String data, boolean isContainTwoKey) {
        String[] returnoneKey = new String[3];
        returnoneKey[0] = data.substring(headIndex, oneParagraphXIndex).substring(1);
        if (isContainTwoKey) {
            returnoneKey[1] = data.substring(oneParagraphXIndex, twoParagraphXIndex).substring(1);
            returnoneKey[2] = data.substring(twoParagraphXIndex, twoParagraphXIndex + 6);
        } else {
            returnoneKey[1] = data.substring(oneParagraphXIndex, oneParagraphXIndex + 6);
        }
        return returnoneKey;
    }

    /**
     * 向加密机发送二进制数据
     *
     * @param reqMessage
     *            报文非加密/解密部分
     * @param data
     *            加密/解密内容
     * @return 加密机返回信息
     * @throws IOException
     *             IO异常抛出
     */
    private byte[] sendBiary(String reqMessage, byte[] data) throws IOException {
        byte[] b = reqMessage.getBytes();
        int i = b.length;
        // 扩容数组
        b = Arrays.copyOf(b, b.length + data.length);
        // 将加密/解密内容加到数组中
        System.arraycopy(data, 0, b, i, data.length);
        return sendBytes(b);
    }

}