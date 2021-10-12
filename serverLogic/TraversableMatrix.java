package serverLogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.*;
import modules.*;

/**
 * This class implements adapter/wrapper/decorator design pattern
 */
public class TraversableMatrix implements Traversable<Index> {
    protected final Matrix matrix;
    protected Index startIndex;
    protected final DfsAlgo dfsAlgorithm = new DfsAlgo();

    public TraversableMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void setStartIndex(Index startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public Node<Index> getOrigin() throws NullPointerException {
        if (this.startIndex == null) throw new NullPointerException("start index is not initialized");
        return new Node<>(this.startIndex);

    }
    /**
     @param  someNode This is the first parameter to getReachableNodes method
     @param  isZeroReachable This is the second parameter to getReachableNodes method
     @param withDiagonals This is the Third parameter to getReachableNodes method
     The function will get a Node<Index> and 2 more variables that will determined if
     we are searching neighbors including diagonal in the matrix or if the a node with value 0 is reachable.
     The function will gather all the neighbors in a List and will return it.
     @return Collection<Node<Index>>
     */
    @Override
    public Collection<Node<Index>> getReachableNodes(Node<Index> someNode, boolean isZeroReachable, boolean withDiagonals) {
        List<Node<Index>> reachableIndices = new ArrayList<>();

        for (Index index : this.matrix.getNeighbors(someNode.getData(), withDiagonals)) {
            if (matrix.getValue(index) == 1 || (isZeroReachable && matrix.getValue(index) >= 0)) {
                Node<Index> indexNode = new Node<>(index, someNode);
                reachableIndices.add(indexNode);
            }
        }

        return reachableIndices;
    }

    @Override
    public String toString() {
        return matrix.toString();
    }

    /**
     * Finds the shortest path to the dest index
     *
     * @param src Where from should we start
     * @param dest To where do we want to get
     * @param isZeroReachable Can we go through nodes with the value of zero
     * @return The shortest path from one node to another
     */
    public List<List<Index>> getMinimumPath(Index src, Index dest, boolean isZeroReachable) {
        List<List<Index>> allPaths = dfsAlgorithm.getAllPathsFromSrcToDest(this, src, dest, isZeroReachable);
        return getLightestPath(allPaths);
    }


    /**
     * Gets the lightest path(s) out of all the given paths. Question 4
     *
     * @param allPaths Holds all the possible paths from one node to another
     * @return The minimal path(s)
     */
    private List<List<Index>> getLightestPath(List<List<Index>> allPaths) {
        List<List<Index>> minPaths = new ArrayList<>();
        int min = Integer.MAX_VALUE;

        // Finds the lightest path out of all paths
        for (List<Index> list : allPaths) {
            int result = 0;
            for (Index i : list) {
                result += matrix.getValue(i);
            }
            if (result < min) {
                min = result;
            }
        }

        // To get the other minimal paths, that are in the same size as we found before
        for (List<Index> list : allPaths) {
            int result = 0;
            for (Index i : list) {
                result += matrix.getValue(i);
            }
            if (result == min) {
                minPaths.add(list);
            }
        }

        return minPaths;
    }


    /**
     * Used for question 3. main function of it.
     * @return the amount of valid submarines
     */
    public int calcSubmarines() {
        int result = 0;
        List<HashSet<Index>> possibleSubmarines = findSCC();    // Find all the strongly connected component

        // Go through each component and check if it's a valid submarine
        for (HashSet<Index> component : possibleSubmarines) {
            if (isSubmarine(component)) {
                result++;
            }
        }

        return result;
    }


    /**
     * Used for question 3. Find strongly connected components for question 3. uses DFS from each unvisited node.
     * @return a list of all the SCC
     */
    private List<HashSet<Index>> findSCC() {
        List<HashSet<Index>> connectionComponentsList = new ArrayList<>(); // Will contain a list of connected components
        HashSet<Node<Index>> visited = new HashSet<>(); // Hashset of indices we already visited
        int sizeOfMatrix = this.matrix.getSizeOfMatrix();

        // Go through each unvisited node and run DFS algo from him (as source vertex), then add the vertices we have visited the the list
        for (int i = 0; i < sizeOfMatrix; i++) {
            for (int j = 0; j < sizeOfMatrix; j++) {
                Index sourceIndex = new Index(i, j);
                Node<Index> aNode = new Node<>(sourceIndex);    // This is used to so you will be able to perform the "contains" if statement below

                if (this.matrix.getValue(sourceIndex) == 1 && !(visited.contains(aNode))) {
                    setStartIndex(sourceIndex);
                    connectionComponentsList.add((HashSet<Index>) new ThreadLocalDFS<Index>().traverse(this, visited));
                }
            }
        }

        connectionComponentsList.sort(Comparator.comparingInt(Set::size)); // Sort the list by size
        return connectionComponentsList;
    }

    /**
     * Validates if a SCC is actually a real submarine
     * @param component The SCC given
     * @return is it a valid submarine?
     */
    private boolean isSubmarine(HashSet<Index> component) {
        if (component.size() < 2) {
            return false;
        }  // A submarine needs to have at least 2 slots

        Index minCol = component.stream().min(Comparator.comparing(Index::getColNum)).orElseThrow(NoSuchElementException::new);
        Index minRow = component.stream().min(Comparator.comparing(Index::getRowNum)).orElseThrow(NoSuchElementException::new);
        Index maxCol = component.stream().max(Comparator.comparing(Index::getColNum)).orElseThrow(NoSuchElementException::new);
        Index maxRow = component.stream().max(Comparator.comparing(Index::getRowNum)).orElseThrow(NoSuchElementException::new);

        // Make sure that every slot inside the "square" is 1 ( a valid submarine )
        for (int i = minRow.getRowNum(); i < maxRow.getRowNum(); i++) {
            for (int j = minCol.getColNum(); j < maxCol.getColNum(); j++) {
                if (this.matrix.getValue(new Index(i, j)) != 1) {
                    return false;
                }
            }
        }

        return true;
    }
}

