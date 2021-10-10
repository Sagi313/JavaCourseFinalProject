package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TcpServer {

    private final int port; // initialize in constructor
    private volatile boolean stopServer; // volatile - stopServer variable is saved in RAM memory
    private ThreadPoolExecutor threadPool; // handle each client in a separate thread
    private IHandler requestHandler; // what is the type of clients' tasks

    public TcpServer(int port){
        this.port = port;
        this.threadPool = null;
        stopServer = false;
    }

    public void supportClients(IHandler handler) {
        this.requestHandler = handler;
        /*
         A server can do many things. Dealing with listening to clients and initial
         support is done in a separate thread
         */
        Runnable mainServerLogic = () -> {  //corePoolSize= available thread; maximumPoolSize= the max amount of available threads to use; keepAliveTime= number of seconds before killing a thread; LinkedBlockingQueue= The data structure the keeps the thread inside (can't add when full, and can't get when empty)
            this.threadPool = new ThreadPoolExecutor(3,5,
                    10, TimeUnit.SECONDS, new LinkedBlockingQueue());
            /*
            2 Kinds of sockets
            Server Socket - a server sockets listens and wait for incoming connections
            1. server socket binds to specific port number
            2. server socket listens to incoming connections
            3. server socket accepts incoming connections if possible

            Operational socket (client socket)
             */
            try {
                ServerSocket serverSocket = new ServerSocket(this.port); // Creates and also binds the socket (step 1)
                /*
                listen to incoming connection and accept if possible
                be advised: accept is a blocking call
                 */
                System.out.println("Server is listening...");

                while(!stopServer){
                    Socket serverClientConnection = serverSocket.accept(); // step 2+3 (listen and establish)
                    // define a task and submit to our threadPool
                    Runnable clientHandling = ()->{
                        System.out.println("Server: Handling a client");
                        try {
                            requestHandler.handle(serverClientConnection.getInputStream(),
                                    serverClientConnection.getOutputStream());
                        } catch (IOException | ClassNotFoundException ioException) {
                            ioException.printStackTrace();
                        }
                        // terminate connection with client
                        try {
                            serverClientConnection.close(); // Closing the server connection only once
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    };
                    threadPool.execute(clientHandling);
                }
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        new Thread(mainServerLogic).start();
    }

    public void stop(){
        if(!stopServer){
            stopServer = true;
            if(threadPool!=null)
                threadPool.shutdown();
        }

    }

    public static void main(String[] args) {
        TcpServer webServer = new TcpServer(5555);
        webServer.supportClients(new MatrixIHandler());

    }
}
