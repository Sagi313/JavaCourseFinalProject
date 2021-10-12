package serverLogic;
import modules.*;

import java.util.Collection;

/**
 * This interface defines the functionality required for a traversable graph
 * Helps us run on a matrix as a graph
 */
public interface Traversable<T> {
    public Node<T> getOrigin();
    public Collection<Node<T>>  getReachableNodes(Node<T> someNode, boolean isZeroReachable, boolean withDiagonals);
    }

