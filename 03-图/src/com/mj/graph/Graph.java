package com.mj.graph;

import java.util.List;
import java.util.Set;

public abstract class Graph<V, E> {

    public Graph(WeightManager<E> weightManager) {
        this.weightManager = weightManager;
    }

    public Graph() {}
    public abstract int edgesSize();
    public abstract int verticesSize();

    public abstract void addVertex(V v);

    public abstract void addEdge(V from, V to);
    public abstract void addEdge(V from, V to, E weight);

    public abstract void removeVertex(V v);
    public abstract void removeEdge(V from, V to);

    public abstract void bfs(V v, vertexVisitor<V> visitor);
    public abstract void dfs(V v, vertexVisitor<V> visitor);

    public abstract List<V> topologicalSort();

    public abstract Set<EdgeInfo<V, E>> mst();

    protected WeightManager<E> weightManager;

    public interface WeightManager<E> {
        int compare(E w1, E w2);
        E add(E w1, E w2);
    }

    interface vertexVisitor<V> {
        boolean visit(V v);
    }

    public static class EdgeInfo<V, E> {
        private V from;
        private V to;
        private E weight;

        public EdgeInfo(V from, V to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "EdgeInfo{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }
}
