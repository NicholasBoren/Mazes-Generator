package disjointsets;


import java.util.ArrayList;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private List<T> nodes;
    private int size;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        nodes = new ArrayList<>();
        pointers = new ArrayList<>();
        size = 0;
    }

    @Override
    public void makeSet(T item) {
        nodes.add(item);
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {

        int index = nodes.indexOf(item); //maybe this could not exist?
        if (index == -1)
        {
            throw new IllegalArgumentException();
        }

        ArrayList<Integer> visited = new ArrayList<>();
        while (pointers.get(index) >= 0) {
            visited.add(index);
            index = pointers.get(index);
        }

        for (Integer vist : visited) {
           pointers.set(vist, index);
        }

        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int rootA = findSet(item1);
        int rootB = findSet(item2);
        if (rootA == rootB) {
            return false;
        }

        if (-1 * pointers.get(rootA) <= -1 * pointers.get(rootB)) {
            pointers.set(rootB, pointers.get(rootB) + pointers.get(rootA));
            pointers.set(rootA, rootB);
        } else {
            pointers.set(rootA, pointers.get(rootA) + pointers.get(rootB));
            pointers.set(rootB, rootA);
        }

        return true;
    }

    /*
    union(A, B):
    rootA = find(A)
    rootB = find(B)
    use -1 * array[rootA] and -1 * array[rootB] to determine weights
    put lighter root under heavier root
     */
}
