package com.yada.sdk.device;

import com.yada.sdk.device.encryption.hsm.EncryptionMachine;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Tx on 2016/9/1.
 */
public class HSMTest {

    String ip;
    int port;
    String lmkZmk;

    @Before
    public void init() {
        ip = "22.7.24.167";
        port = 8;
        lmkZmk = "8E54EEECCC1EB00E779FCF84AC794C7C";
    }

    @Test
    public void GetDekLmk() {
        EncryptionMachine e = new EncryptionMachine(ip, port, null);
        for (String temp : e.getDekKeyArray(lmkZmk)) {
            System.out.println(temp);
        }
    }
}


//-------消息头
// A1   响应吗
// 00   错误代码
// X5E0F1BE46249E342B8153179F37DD920X
// B952C40CDFDF84E35769FAA52DFCB571
// BE2113