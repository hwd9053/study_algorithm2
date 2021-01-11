package com.mj.graph;

import com.mj.MinHeap;
import com.mj.UnionFind;

import java.util.*;

@SuppressWarnings("unchecked")
public class ListGraph<V, E> extends Graph<V, E>{
    private final Map<V, Vertex<V, E>> vertices = new HashMap<>();
    private final Set<Edge<V, E>> edges = new HashSet<>();
    private Comparator<Edge<V, E>> edgeComparator = (Edge<V, E> e1, Edge<V, E> e2) -> {
        return weightManager.compare(e1.weight, e2.weight);
    };

    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }

    public ListGraph() {}

    public void print() {
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
            System.out.println("-------in--------");
            System.out.println(vertex.inEdges);
            System.out.println("-------out--------");
            System.out.println(vertex.outEdges);
        });

        edges.forEach((Edge<V, E> edge) -> {
            System.out.println(edge);
        });
    }

    @Override
    public int edgesSize() {
        return edges.size();
    }

    @Override
    public int verticesSize() {
        return vertices.size();
    }

    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) return;
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        // 判断from，to顶点是否存在
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.weight = weight;

        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }

        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null) return;

        // 如果要一边遍历一边删除的话，用迭代器
        for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext();) {
            Edge<V, E> edge = iterator.next();
            edge.to.inEdges.remove(edge);
            iterator.remove(); // 将当前遍历到的元素从集合中删掉
            edges.remove(edge);
        }

        for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext();) {
            Edge<V, E> edge = iterator.next();
            edge.from.outEdges.remove(edge);
            iterator.remove(); // 将当前遍历到的元素从集合中删掉
            edges.remove(edge);
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            return;
        }

        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            return;
        }

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }

    }

    @Override
    public void bfs(V v, vertexVisitor<V> visitor) {
        if (visitor == null) return;
        Vertex<V, E> beginVertex = vertices.get(v);
        if (beginVertex == null) return;

        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        visitedVertices.add(beginVertex);

        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            if (visitor.visit(vertex.value)) return;

            for (Edge<V, E> edge : vertex.outEdges) {
                if (visitedVertices.contains(edge.to)) continue;
                queue.offer(edge.to);
                visitedVertices.add(edge.to);
            }
        }
    }

    @Override
    public void dfs(V v, vertexVisitor<V> visitor) {
        if (visitor == null) return;
        Vertex<V, E> beginVertex = vertices.get(v);
        if (beginVertex == null) return;

        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();

        stack.push(beginVertex);
        if (visitor.visit(beginVertex.value)) return;
        visitedVertices.add(beginVertex);

        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            for (Edge<V, E> edge : vertex.outEdges) {
                if (!visitedVertices.contains(edge.to)) {
                    stack.push(vertex);
                    stack.push(edge.to);
                    if (visitor.visit(edge.to.value)) return;
                    visitedVertices.add(edge.to);
                    break;
                }
            }
        }
    }

    // 拓扑排序(不断的找入度为0的vertex)
    @Override
    public List<V> topologicalSort() {
        List<V> list = new ArrayList<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>(); // 用来存放入度为0的点
        Map<Vertex<V, E>, Integer> ins = new HashMap<>();

        // 初始化(将入度为0的点放入队列序列)
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            int in = vertex.inEdges.size();
            if (in == 0) {
                queue.offer(vertex);
            } else { // 记录一下最初入度不为0的点
                ins.put(vertex, in);
            }
        });

        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            list.add(vertex.value);
            for (Edge<V, E> edge : vertex.outEdges) {
                int toIn = ins.get(edge.to) - 1;
                if (toIn == 0) {
                    queue.offer(edge.to);
                } else {
                    ins.put(edge.to, toIn);
                }
            }
        }

        return list;
    }

    @Override
    public Set<EdgeInfo<V, E>> mst() {
        return prim();
    }

    private Set<EdgeInfo<V, E>> prim() {
        Iterator<Vertex<V, E>> it = vertices.values().iterator();
        if (!it.hasNext()) return null;

        Set<EdgeInfo<V, E>> edgeInfo = new HashSet<>();
        Set<Vertex<V, E>> addedVertices = new HashSet<>();

        Vertex<V, E> vertex = it.next();
        addedVertices.add(vertex);
        MinHeap<Edge<V, E>> heap = new MinHeap<>(vertex.outEdges, edgeComparator);

        int verticesSize = vertices.size();
        while (!heap.isEmpty() && addedVertices.size() < verticesSize) {
            Edge<V, E> edge = heap.remove();
            if (addedVertices.contains(edge.to)) continue;

            edgeInfo.add(edge.info());
            addedVertices.add(edge.to);
            heap.addAll(edge.to.outEdges); // 这边是全加入堆，然后从堆取的时候判断是否已经有了。也可以加的时候就先判断是否重复了
        }
        return edgeInfo;
    }

    private Set<EdgeInfo<V, E>> kruskal() {
        int edgeSize = vertices.size() - 1;
        if (edgeSize == -1) return null;

        Set<EdgeInfo<V, E>> edgeInfo = new HashSet<>();
        MinHeap<Edge<V, E>> heap = new MinHeap<>(edges, edgeComparator);

        UnionFind<Vertex<V, E>> uf = new UnionFind<>();
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            uf.makeSet(vertex);
        });
        while (!heap.isEmpty() && edgeInfo.size() < edgeSize) {
            Edge<V, E> edge = heap.remove();
            if (uf.isSame(edge.from, edge.to)) continue;

            edgeInfo.add(edge.info());
            uf.union(edge.from, edge.to);
        }

        return edgeInfo;
    }

//    @Override
//    public void bfs(V v) {
//        Vertex<V, E> beginVertex = vertices.get(v);
//        if (beginVertex == null) return;
//
//        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
//        Queue<Vertex<V, E>> queue = new LinkedList<>();
//        queue.offer(beginVertex);
//        visitedVertices.add(beginVertex);
//
//        while (!queue.isEmpty()) {
//            Vertex<V, E> vertex = queue.poll();
//            System.out.println(vertex);
//
//            for (Edge<V, E> edge : vertex.outEdges) {
//                if (visitedVertices.contains(edge.to)) continue;
//                queue.offer(edge.to);
//                visitedVertices.add(edge.to);
//            }
//        }
//    }

//    @Override
//    public void dfs(V v) {
//        Vertex<V, E> beginVertex = vertices.get(v);
//        if (beginVertex == null) return;
//        dfs(beginVertex, new HashSet<>());
//
//    }
//
//    private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertices) {
//        System.out.println(vertex.value);
//        visitedVertices.add(vertex);
//        for (Edge<V, E> edge : vertex.outEdges) {
//            if (visitedVertices.contains(edge.to)) continue;
//            dfs(edge.to, visitedVertices);
//        }
//    }
//
//    // 迭代版dfs
//    public void dfs2(V v) {
//        Vertex<V, E> beginVertex = vertices.get(v);
//        if (beginVertex == null) return;
//
//        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
//        Stack<Vertex<V, E>> stack = new Stack<>();
//
//        stack.push(beginVertex);
//        System.out.println(beginVertex.value);
//        visitedVertices.add(beginVertex);
//
//        while (!stack.isEmpty()) {
//            Vertex<V, E> vertex = stack.pop();
//            for (Edge<V, E> edge : vertex.outEdges) {
//                if (!visitedVertices.contains(edge.to)) {
//                    stack.push(vertex);
//                    stack.push(edge.to);
//                    System.out.println(edge.to.value);
//                    visitedVertices.add(edge.to);
//                    break;
//                }
//            }
//        }
//    }

    private static class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();
        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Vertex)) return false;
            return Objects.equals(value, ((Vertex<V, E>)o).value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        EdgeInfo<V, E> info() {
            return new EdgeInfo<>(from.value, to.value, weight);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) return false;
            Edge<V, E> edge = (Edge<V, E>) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }
}
