package com.myapi.autotest.common;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WebSockListener extends WebSocketClient {
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
    private final CountDownLatch connectedLatch = new CountDownLatch(1);
    private volatile boolean isConnected = false;
    public WebSockListener(URI serverUri) {
        super(serverUri);
    }

    public WebSockListener(String serverUri) {
        super(URI.create(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("WebSockListener onOpen");
        isConnected = true;
        connectedLatch.countDown();
    }

    @Override
    public void onMessage(String message) {
        messageQueue.offer(message);
        System.out.println("WebSockListener onMessage: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSockListener onClose");
        isConnected = false;
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("WebSockListener onError: " + ex.getMessage());
        isConnected = false;
        connectedLatch.countDown();
    }

    public boolean waitForConnection(long timeout,TimeUnit unit) throws InterruptedException {
        return connectedLatch.await(timeout,unit);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void sendMessage(String message) {
        if(isConnected) {
            send(message);
        }
        else{
            throw new IllegalStateException("WebSockListener is not Connected. Cannot send message!");
        }
    }

    public String waitForMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return messageQueue.poll(timeout,unit);
    }
}
