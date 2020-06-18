package Phase4;

import Phase2.HTTpService;
import Phase2.Request;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
    static void run(int maximumNumberOfThreads) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(1726)) {
            System.out.println("Server is started and waiting for clients.");
            int cnt = 0;
            while (cnt++ < maximumNumberOfThreads) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientHandler(socket, cnt));
            }
            pool.shutdown();
            System.out.println("Server Closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private int id;

    ClientHandler(Socket socket, int id) {
        this.id = id;
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Request " + id + " is running.");
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Request request = (Request) in.readObject();
            HTTpService service = new HTTpService(request);
            Response response = service.runService();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(response);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Request " + id + " is closed.");

    }
}