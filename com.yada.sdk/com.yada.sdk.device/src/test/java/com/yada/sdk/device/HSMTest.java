package com.yada.sdk.device;

import com.yada.sdk.device.encryption.hsm.EncryptionMachine;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Tx on 2016/9/1.
 */
public class HSMTest {

    String ip;
    int port;
    String lmkZmk;  //区域密钥
    String lmkDek;  //设备主密钥
    String zmkTmk;  //区域主密钥保护的终端密钥
    String lmkTmk;  //终端主密钥
    String lmkZek;  //通讯主密钥
    String key;     //通讯主密钥加密的密文
    String sposPWD; //spos客户端密码


//    @Before
//    public void init() {
//        ip = "22.188.41.19";
//        port = 8;
//        lmkZmk = "8E54EEECCC1EB00E779FCF84AC794C7C";
//        lmkDek = "B5A754C1926BF597536671364E597C65";
//        zmkTmk = "620D2892F9189CA30BDA03DADB6B1B88";
//        lmkTmk = "B6F0F9262F4C535DC45348A551EE419F";
//        lmkZek = "EC1F355EEDF8A75544150A49FC106A74";
//        sposPWD = "12345678";
//        key = "2DB6ECBE24CBECC8";
//
//    }
//
//    @Test
//    public void GetDekLmk() {
//        EncryptionMachine e = new EncryptionMachine(ip, port, null);
//        for (String temp : e.getDekKeyArray(lmkZmk)) {
//            System.out.println("---"+temp);
//        }
//    }
//
//
//    @Test
//    public void GetZekLmk() {
//        EncryptionMachine e = new EncryptionMachine(ip, port, null);
//        for (String temp : e.getZekKeyArray(lmkDek)) {
//            System.out.println(temp);
//        }
//    }
//
//    @Test
//    public void GetTmkLmk() {
//        EncryptionMachine e = new EncryptionMachine(ip, port, null);
//        for (String temp : e.getTmkKeyArray(zmkTmk, lmkZmk)) {
//            System.out.println(temp);
//        }
//    }
//
//    @Test
//    public void GetTmkDek() {
//        EncryptionMachine e = new EncryptionMachine(ip, port, null);
//        System.out.println(e.getDekTmk(lmkDek, lmkTmk));
//    }
//
//    @Test
//    public void GetDataByDecryption() {
//        EncryptionMachine e = new EncryptionMachine(ip, port, null);
//        System.out.println(e.getDataByDecryption(key, lmkZek));
//    }
//
//    @Test
//    public void GetZekPwd() {
//        EncryptionMachine e = new EncryptionMachine(ip, port, null);
//        System.out.println(e.getDataByEncryption(sposPWD, lmkZek));
//    }
}


