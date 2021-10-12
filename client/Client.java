package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import modules.*;


public class Client {

    /**
     * This class represents a client in the system that tries to connect into the server. This file will be located
     * on the user's side originally.  It creates a connection to the server, sends it the data and the
     * question number and wait for the answer.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket =new Socket("127.0.0.1",5555);
        System.out.println("Socket got created");

        ObjectOutputStream toServer=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream fromServer=new ObjectInputStream(socket.getInputStream());

        int[][] sourceMatrix1 = {
                {0, 1, 1},
                {0, 1, 1},
                {0, 1, 1},
        };
        int[][] sourceMatrix2 = {
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 0},
                {1, 0, 0, 0, 1},
                {1, 1, 0, 1, 0},
                {1, 0, 0, 1, 0},
        };
        int[][] sourceMatrix3 = {
                {1, 0, 1, 0, 1, 1, 0, 1, 1, 0},
                {0, 0, 1, 0, 0, 1, 0, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                {1, 1, 1, 1, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {1, 1, 0, 1, 0, 1, 0, 1, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 0, 1, 0},
                {1, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {1, 1, 1, 1, 0, 1, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 1, 1, 1},
        };
        int[][] sourceMatrix4 = {
                {100, 100, 100},
                {500, 900, 300},
                {400, 100, 0},
        };
        int[][] sourceMatrix5 = {
                {100, 100, 100, 500, 300},
                {500, 900, 300, 200, 200},
                {400, 100, 200, 0, 250},
                {400, 100, 500, 800, 350},
                {100, 300, 200, 600, 100},
        };

        Index sourceIndex = new Index (1,1);
        Index destIndex = new Index(1,2);   // Relevant only for a few questions

        /* Question 1 */
        toServer.writeObject("quest1");
        toServer.writeObject(sourceMatrix1);
        toServer.writeObject(sourceIndex);

        if (sourceMatrix1[sourceIndex.getRowNum()][sourceIndex.getColNum()] == 0){  // Invalid input base case
            toServer.writeObject("stop");
            throw new IllegalArgumentException("The source index cannot be an unreachable node");
        }

        List<Index> reachableIndices =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("\nQuestion One- The reachable indices are: "+ reachableIndices);


        /* Question 2 */
        toServer.writeObject("quest2");

        toServer.writeObject(sourceMatrix1);
        toServer.writeObject(sourceIndex);
        toServer.writeObject(destIndex);

        List<Index> allShortestPaths =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("\nQuestion Two- The shortest path to the destination is: "+ allShortestPaths);


        /* Question 3 */
        toServer.writeObject("quest3");
        toServer.writeObject(sourceMatrix1);

        int numOfSubmarines = (int)fromServer.readObject();


        System.out.println("\nQuestion Three- The number of submarines is: "+ numOfSubmarines);


        /* Question 4 */
        toServer.writeObject("quest4");

        toServer.writeObject(sourceMatrix4);
        toServer.writeObject(sourceIndex);
        toServer.writeObject(destIndex);

        List<Index> allLightestPaths =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("\nQuestion Four- The lightest path to the destination is: "+ allLightestPaths);


        /* Stop communication */
        toServer.writeObject("stop");
        System.out.println("\nClosing all streams");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("\nConnection killed gracefully. Task is finished");
    }
}

