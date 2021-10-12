package serverLogic;

import modules.*;
import java.util.*;


public class ThreadLocalDFS<T> {
    final ThreadLocal<Stack<Node<T>>> threadLocalStack = ThreadLocal.withInitial(Stack::new);
    final ThreadLocal<Set<Node<T>>> threadLocalSet = ThreadLocal.withInitial(LinkedHashSet::new);

    /**
     * To be able to run the DFS in a few threads we need to keep the data structures separated in each thread.
     * We will do this with thread safe objects
     *
     * @param someGraph the graph that was given by the user
     * @return the connected component
     */

    public List<T> traverse(Traversable<T> someGraph) {
        threadLocalStack.get().push(someGraph.getOrigin());
        while (!threadLocalStack.get().isEmpty()) {
            Node<T> popped = threadLocalStack.get().pop();
            threadLocalSet.get().add(popped);
            Collection<Node<T>> reachableNodes = someGraph.getReachableNodes(popped, false, true);
            for (Node<T> singleReachableNode : reachableNodes) {
                if (!threadLocalSet.get().contains(singleReachableNode) && !threadLocalStack.get().contains(singleReachableNode)) {
                    threadLocalStack.get().push(singleReachableNode);
                }
            }
        }
        List<T> connectedComponent = new ArrayList<>();
        for (Node<T> node : threadLocalSet.get()) connectedComponent.add(node.getData());
        return connectedComponent;
    }

    /**
     * To be able to run the DFS in a few threads we need to keep the data structures separated in each thread.
     * We will do this with thread safe objects. Used only in question 3
     *
     * @param someGraph someGraph the graph that was given by the user
     * @param visitedIndexes To make sure we don't go over nodes we have already scanned from a different source node
     * @return the connected component
     */
    public HashSet<T> traverse(Traversable<T> someGraph, HashSet<Node<T>> visitedIndexes) {
        threadLocalStack.get().push(someGraph.getOrigin());
        while (!threadLocalStack.get().isEmpty()) {
            Node<T> popped = threadLocalStack.get().pop();
            threadLocalSet.get().add(popped);
            Collection<Node<T>> reachableNodes = someGraph.getReachableNodes(popped, false, false);

            for (Node<T> singleReachableNode : reachableNodes) {
                if (!threadLocalSet.get().contains(singleReachableNode) && !threadLocalStack.get().contains(singleReachableNode)) {
                    threadLocalStack.get().push(singleReachableNode);

                    visitedIndexes.add(singleReachableNode);
                }
            }
        }
        HashSet<T> connectedComponent = new HashSet<>();
        for (Node<T> node : threadLocalSet.get()) connectedComponent.add(node.getData());
        return connectedComponent;
    }
}

