package com.yada.sdk.device.encryption.hsm;

import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * 加密机通讯的客户端
 * Created by fengming on 3/31/17.
 */
public class HsmClient {
    private final Logger logger = LoggerFactory.getLogger(HsmClient.class);
    private final InetSocketAddress address;
    private final int readTimeout;

    public HsmClient(String ip, int port, int readTimeout) {
        this.address = new InetSocketAddress(ip, port);
        this.readTimeout = readTimeout;
    }

    ByteBuffer send(ByteBuffer req) {
        TcpClient client = new TcpClient(address,
                new FixLenPackageSplitterFactory(2, false), readTimeout);
        try {
            logger.info("send to Hsm: {}", Hex.encodeHexString(req.array()));
            client.open();
            ByteBuffer rece = client.send(req);
            logger.info("rece from Hsm: {}", Hex.encodeHexString(rece.array()));
            return rece;
        } catch (IOException e) {
            logger.warn(String.format("%s has a error...", address.toString()), e);
            return null;
        } finally {
            client.close();
        }
    }
}
