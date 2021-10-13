package server;

import java.io.*;
import java.util.Collection;
import modules.*;
import serverLogic.*;


public class MatrixIHandler implements IHandler {
    private Matrix matrix;

    /**
     * This class handles all the calculation required by the server to perform on the client's matrix. It gets the
     * question number and parameters and responds with the correct answer
     *
     * @param fromClient is the data stream that we are getting from the socket with the clint.
     * @param toClient is the data that we will send in the socket to the client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {
        /*
        Send data as bytes.
        Read data as bytes then transform to meaningful data
        ObjectInputStream and ObjectOutputStream can read and write both primitives and objects
         */
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        boolean running = true;
        // handle client's tasks
        while(running){

            switch (objectInputStream.readObject().toString()){

                case "quest1":{
                    /*
                    * Calculates and returns to the client the number of reachable indices from the given index.
                    * This works using DFS algorithm from the source index. Indices with "1" will count as alive.
                    */
                    System.out.println("\nQuestion 1 Started");

                    int[][] tempMatrix = (int[][])objectInputStream.readObject();
                    Index tempIndex = (Index) objectInputStream.readObject();

                    this.matrix = new Matrix(tempMatrix);

                    TraversableMatrix matrixTrav = new TraversableMatrix(this.matrix);
                    matrixTrav.setStartIndex(tempIndex);

                    System.out.println("Got and applied all the parameters");

                    ThreadLocalDFS dfs = new ThreadLocalDFS();
                    Collection<Node<Index>> neighbors = dfs.traverse(matrixTrav);

                    objectOutputStream.writeObject(neighbors);

                    System.out.println("Question 1 is finished successfully");
                    break;
                }

                case "quest2":{
                    /*
                    * Using DFS we find all the paths to the destination index.
                    */
                    System.out.println("\nQuestion 2 Started");

                    int[][] tempArray = (int[][])objectInputStream.readObject();
                    this.matrix = new Matrix(tempArray);

                    TraversableMatrix matrixTrav = new TraversableMatrix(this.matrix);
                    Index sourceIndex = (Index)objectInputStream.readObject();
                    Index destIndex=(Index)objectInputStream.readObject();
                    matrixTrav.setStartIndex(sourceIndex);

                    System.out.println("Got and applied all the parameters");

                    objectOutputStream.writeObject(matrixTrav.getMinimumPath(sourceIndex, destIndex, false));

                    System.out.println("Question 2 is finished successfully");
                    break;
                }

                case "quest3":{
                    /*
                    * Returns to the client the number of valid submarines in the matrix.
                    * This first runs DFS to find the SCC. then, it checks to see if each
                    * component is really a valid submarine.
                    */
                    System.out.println("\nQuestion 3 Started");

                    int[][] tempArray = (int[][])objectInputStream.readObject();
                    this.matrix = new Matrix(tempArray);
                    TraversableMatrix matrixTrav = new TraversableMatrix(this.matrix);

                    System.out.println("Got and applied all the parameters");

                    int numOfSubmarines = matrixTrav.calcSubmarines();
                    objectOutputStream.writeObject(numOfSubmarines);

                    System.out.println("Question 3 is finished successfully");
                    break;
                }

                case "quest4":{
                    System.out.println("\nQuestion 4 Started");

                    int[][] tempArray = (int[][])objectInputStream.readObject();
                    this.matrix = new Matrix(tempArray);
                    TraversableMatrix matrixTrav = new TraversableMatrix(this.matrix);

                    Index sourceIndex = (Index)objectInputStream.readObject();
                    Index destIndex=(Index)objectInputStream.readObject();
                    matrixTrav.setStartIndex(sourceIndex);

                    System.out.println("Got and applied all the parameters");

                    objectOutputStream.writeObject(matrixTrav.getMinimumPath(sourceIndex, destIndex, true));

                    System.out.println("Question 4 is finished successfully");
                    break;
                }

                case "stop":{
                    System.out.println("\nConnection is closed gracefully (stop command)");
                    running = false;
                    break;
                }
            }
        }


    }


}
