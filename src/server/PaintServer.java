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

//todo rework the PaintServer class to work with BlockingQueues for host server communication
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
        try {
            this.host.start();
//            while (this.host == null) {
//                this.host = this.serverSocket.accept();
//                if (!this.host.getInetAddress().equals(InetAddress.getLoopbackAddress())) {
//                    this.host.close();
//                    this.host = null;
//                }
//                this.connections.add(host);
//                ObjectOutputStream oos = new ObjectOutputStream(host.getOutputStream());
//                oos.writeObject(this.canvasObjects);
//                this.clientExecutor.execute(new ClientRequestOverseer(host, this));
//            }

            for (; ; ) {
                Socket newConnection = this.serverSocket.accept();
                this.connections.add(newConnection);
                this.clientExecutor.execute(new ConnectionRequestOverseer(newConnection, this));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public boolean notifyClients() {
        return this.clientNotifier.notifyClients(this.connections,this.canvasObjects);
    }

    @Override
    public boolean notifyClients(Socket harbingerClient) {
        return this.clientNotifier.notifyClients(harbingerClient, this.connections,this.canvasObjects, this.hostClientActions);
    }

    @Override
    public boolean openServer() throws IOException {
        System.out.println("server Opened");
        for (; ; ) {
            Socket newConnection = this.serverSocket.accept();
            this.connections.add(newConnection);
            this.clientExecutor.execute(new ConnectionRequestOverseer(newConnection, this));
        }
    }
}
