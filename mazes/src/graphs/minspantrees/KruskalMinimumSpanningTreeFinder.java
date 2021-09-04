package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        Collection<V> vertices = graph.allVertices();
        if (vertices.size() <= 1) {
            return new MinimumSpanningTree.Success<>();
        }

        List<E> edges = new ArrayList<>(graph.allEdges());
        DisjointSets<V> disjointSets = createDisjointSets();
        edges.sort(Comparator.comparingDouble(E::weight));

        Collection<E> finalMST = new ArrayList<>();
        for (V ver : vertices) {
            disjointSets.makeSet(ver);
        }


        for (E edge : edges) {
            // V uMST = edge.from();
            // V vMST = edge.to();
            int uMST =  disjointSets.findSet(edge.from());
            int vMST = disjointSets.findSet(edge.to());
            if (uMST != vMST) {
                finalMST.add(edge);
                disjointSets.union(edge.from(), edge.to());
            }
        }

        MinimumSpanningTree<V, E> success = new MinimumSpanningTree.Success<V, E>(finalMST);
        if (finalMST.size() == graph.allVertices().size() - 1) {
            return success;
        } else {
            MinimumSpanningTree<V, E> failure = new MinimumSpanningTree.Failure<>();
            return failure;
        }
    }
}
