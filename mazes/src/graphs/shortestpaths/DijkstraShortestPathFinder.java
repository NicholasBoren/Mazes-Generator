package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();


        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        if (end.equals(start))
        {
            return new HashMap<V, E>();
        }

        Set<V> know = new HashSet<>();
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> edgeTo = new HashMap<>();
        ExtrinsicMinPQ<V> pq = createMinPQ();
        distTo.put(start, 0.0);
        pq.add(start, 0.0);
        know.add(start); // pulled this out cause we need to start somewehre instead of an empty list

        while (!pq.isEmpty()) { // pq.peak    !know.contains(end) && !pq.isEmpty()
            V u = pq.removeMin();
            know.add(u);
            Collection<E> unknownNeighbors = graph.outgoingEdgesFrom(u); // changed the name to unknownNeighbors

            for (E edge : unknownNeighbors) {

                if (!know.contains(edge.to()) || !know.contains(edge.from())) {

                    double oldDist = distTo.getOrDefault(edge.to(), Double.POSITIVE_INFINITY);
                    double newDist = distTo.getOrDefault(u, Double.POSITIVE_INFINITY) + edge.weight();

                    if (newDist < oldDist) {
                        distTo.put(edge.to(), newDist);
                        edgeTo.put(edge.to(), edge); //fi here from edge.from to edge.to
                        if (pq.contains(edge.to()))         // comparing the
                        {
                            pq.changePriority(edge.to(), newDist);
                        }
                        else {
                            pq.add(edge.to(), newDist); //  changed to cumulative distance
                        }
                    }
                }
            }
            if (u.equals(end))
            {
                break;
            }
        }

        return edgeTo;
    }


    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (spt.isEmpty() || !spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }

        E edge = spt.get(end); // we are assuming that the above method is running right

        List<E> path = new ArrayList<>();
        path.add(edge);

        while (!edge.from().equals(start)) {  //edge is null at one point? edge cases
            edge = spt.get(edge.from());
            path.add(0, edge);
        }

        return new ShortestPath.Success<>(path);
    }
}
