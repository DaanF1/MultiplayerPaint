package server;

import canvas.CanvasObject;
import client.PaintClientCallback;
import client.clientaction.ClientAction;
import client.clientaction.RecieveServerActionsList;
import server.overseer.ConnectionRequestOverseer;
import server.overseer.HostRequestOverseer;
import server.serveraction.ServerAction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PaintServer implements Runnable, PaintServerCallback {
    private final PaintClientCallback paintClientCallback;
    private ArrayList<CanvasObject> canvasObjects;
    private Thread host;
    private ServerSocket serverSocket;
    private List<Socket> connections;
    private ExecutorService clientExecutor;
    private BlockingQueue<ServerAction> hostServerActions;
    private BlockingQueue<ClientAction> hostClientActions;
    private ClientNotifier clientNotifier;
    private Thread connectionListener;


//    public PaintServer(int port) throws IOException {
//        this.canvasObjects = new ArrayList<>();
//        this.serverSocket = new ServerSocket(port);
//        this.connections = new ArrayList<>();
//        this.clientExecutor = Executors.newCachedThreadPool();
//    }

    public PaintServer(int port, BlockingQueue<ClientAction> hostClientActions, PaintClientCallback paintClientCallback) throws IOException {
        this.canvasObjects = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
        this.connections = new ArrayList<>();
        this.clientExecutor = Executors.newCachedThreadPool();
        this.hostClientActions = hostClientActions;
        this.hostServerActions = new LinkedBlockingQueue<>();
        this.hostClientActions.add(new RecieveServerActionsList(this.hostServerActions));
        this.clientNotifier = new ClientNotifier();
        this.paintClientCallback = paintClientCallback;
        this.host = new Thread(new HostRequestOverseer(this.hostServerActions, this));
    }

    @Override
    public void run() {
        this.host.start();
    }

    public boolean stop() {
        try {
            this.host.interrupt();
            this.clientExecutor.shutdownNow();
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<CanvasObject> getCanvasObjects() {
        return this.canvasObjects;
    }

    @Override
    public boolean notifyClients(ClientNotifier.NotificationType notificationType) {
        return this.clientNotifier.notifyClients(this.connections,this, notificationType);
    }

    @Override
    public boolean notifyClients(ClientNotifier.NotificationType notificationType, Socket harbingerClient) {
        return this.clientNotifier.notifyClients(harbingerClient, this.connections, this.hostClientActions, this, notificationType);
    }

    @Override
    public boolean openServer() {
        System.out.println("Server Opened!");
        this.connectionListener = new Thread(() -> {for (; ; ) {
            Socket newConnection = null;
            try {
                newConnection = this.serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.connections.add(newConnection);
            this.clientExecutor.execute(new ConnectionRequestOverseer(newConnection, this));
        }});
        this.connectionListener.start();
        return true;
    }
}
