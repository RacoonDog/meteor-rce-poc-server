package io.github.racoondog.meteor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class SwarmServerSocket extends Thread implements AutoCloseable {
    public final ServerSocket socket;
    private final CopyOnWriteArrayList<SwarmConnection> connections = new CopyOnWriteArrayList<>();

    public SwarmServerSocket(int port) throws IOException {
        this.socket = new ServerSocket(port);
        start();
    }

    @Override
    public void run() {
        while (!isInterrupted() && !this.socket.isClosed()) {
            try {
                Socket connection = this.socket.accept();
                connections.add(new SwarmConnection(connection));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        for (var connection : connections) connection.close();
        this.socket.close();
    }
}
