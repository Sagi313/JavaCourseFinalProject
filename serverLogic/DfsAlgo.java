package serverLogic;

import modules.*;
import java.util.*;

public class DfsAlgo<T> {
/**
 *  This class will give us all the paths from a source index to a destination index
 * {@link #getAllPathsFromSrcToDest} is a method that uses {@link #dfs} to get all the
 * possible paths from one node to another. this is using DFS algo
 */
    private List<List<Index>> allPaths;

    public List<List<Index>> getAllPathsFromSrcToDest(Traversable<T> partOfGraph, Index src, Index dest, boolean isZeroReachable) {
        allPaths = new ArrayList<>();
        ArrayList path = new ArrayList();
        List<Index> visited = new ArrayList<>();
        path.add(src);

        dfs(partOfGraph, src, dest, path, visited, isZeroReachable);

        return allPaths;
    }

    private void dfs(Traversable<T> partOfGraph, Index src, Index dest, ArrayList path, List<Index> visited, boolean isZeroReachable) {
        visited.add(src);
        if (src.equals(dest)) {
            allPaths.add(new ArrayList<>(path));
        }

        for (Node<T> indexNode : partOfGraph.getReachableNodes(new Node<>((T) src), isZeroReachable, true)) {
            // Check the index only if it's not visited yet
            if (!visited.contains(indexNode.getData())) {
                path.add(indexNode.getData());
                dfs(partOfGraph, (Index) indexNode.getData(), dest, path, visited, isZeroReachable);
                path.remove(indexNode.getData());
            }
        }
        visited.remove(src);

    }


}
