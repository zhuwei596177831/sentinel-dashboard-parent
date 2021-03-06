package com.alibaba.csp.sentinel.transport.heartbeat.client;

import com.alibaba.csp.sentinel.transport.endpoint.Protocol;
import com.alibaba.csp.sentinel.transport.ssl.SslFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Leo Li
 */
public class SocketFactory {

    private static class SSLSocketFactoryInstance {
        private static final SSLSocketFactory SSL_SOCKET_FACTORY = SslFactory.getSslConnectionSocketFactory().getSocketFactory();
    }

    public static Socket getSocket(Protocol protocol) throws IOException {
        return protocol == Protocol.HTTP ? new Socket() : SSLSocketFactoryInstance.SSL_SOCKET_FACTORY.createSocket();
    }
}
