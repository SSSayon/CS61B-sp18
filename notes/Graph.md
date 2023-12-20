# Graph

## DFS

### Recursive Implementation

### Application: Topological Sort

- 对每个还未被标记的顶点进行 DFS，不同的 DFS 之间不删除标记记号
- 记录 DFS post order，最后倒序输出即为拓扑顺序

- When nodes are sorted in diagram, arrows all point rightwards.

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220200038_image-20231220195807774.png" style="zoom: 67%;" />

| Problem          | Solution | Time          | Space       |
| ---------------- | -------- | ------------- | ----------- |
| Topological Sort | DFS      | $\Theta(V+E)$ | $\Theta(V)$ |

### Iterative DFS

和下面的 BFS 较为类似，只是用 Stack 存储待访问点而不是用 Queue

#### Implementation 1

```java
private void dfs(Graph G, int s) {
    Stack<Integer> stack = new Stack<Integer>();
    stack.push(s);
    while (!stack.isEmpty()) {
        int v = stack.pop();
        if (!marked[v]) {
            marked[v] = true; // 这时候才标记
            for (int w: G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    stack.push(w);
                }
            }
        }
    }
}
```

- 注意：在 push 顶点进 stack 时不能标记，而是在 pop 的时候标记！

  例子：<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220201313_image-20231220201306429.png" alt="image-20231220201306429" style="zoom:50%;" /> push 时就标记 D 会使访问 B 后不会再把 D push 进去，导致可能出现 A - B - C - D 的错误的 DFS 顺序。

- 因此，空间复杂度为 $\Theta(V+E)$ 而不是 $\Theta(V)$，因为一个顶点可能被 push 进多次

#### Implementation 2

- A memory efficient version, 空间复杂度 $\Theta(V)$ 

```java
public void dfs(Graph G, int s) {
    marked = new boolean[G.V()];
    Iterator<Integer>[] adj = (Iterator<Integer>[]) 
                               new Iterator[G.V()];
    for (int v = 0; v < G.V(); v++)
        adj[v] = G.adj(v).iterator(); // 分别 track 每个顶点的 adj
    
    Stack<Integer> stack = new Stack<Integer>();
    marked[s] = true;
    stack.push(s);
    while (!stack.isEmpty()) {
        int v = stack.peek(); // 得益于上述 track，这里只需用 peek
        if (adj[v].hasNext()) {
            int w = adj[v].next();
            if (!marked[w]) {
                marked[w] = true;
                edgeTo[w] = v;
                stack.push(w);
            }
        }
        else {
            stack.pop(); // 所有 adj 均已访问，才 pop
        }
    }
}
```

- 对每个顶点都用一个 Iterator 记录需要被 visit 的下一个 adjacency 是谁

- visit 完一个顶点，只需一个一个加 adj (然后将其底下的 visit 完)，而不用一次性全 push 进去



## BFS

原理上面都大概讲了，其实应该先写 BFS 再写 Iterative DFS 的，只是为了归类。

用 Queue 记录待访问点，实现与上面的 Iterative DFS 1 基本一样。



## A Warm-Up Problem

一个小练习，给定一张无向图，判断是否有环。

### Solution 1: DisjointSet

对每条边，检查所连的两个顶点是否 connected。若否，union them；若是，则说明有环。

- 时间复杂度：初始化 $O(V)$，进行操作 $O(E\log^*V)$，总共 $O(V+E\log^*V)$ 

### Solution 2: DFS

从任意一点开始 DFS，同时记录 `EdgeTo`。如果有某个顶点 `v` 的一个 adj `w` 已被标记过，同时 `EdgeTo[w] != v`，说明有环。

- 时间复杂度：$\Theta(V+E)$ 



## Shortest Path Tree

- SPT 是树（没有环 + 没有顶点有两个父亲）

- 恰有 $V-1$ 条边

### Dijkstra's Algorithm

用优先级队列 PQ 维护所有顶点，所有顶点距 source 距离初始化为 $\infty$。依次将最小距离的顶点加入 SPT 中，并更新相连顶点的距离信息，同时在 PQ 中 decrease priority。

时间复杂度：考虑 PQ 的各操作

- insertion: $V$, each $O(\log V)$ 
- delete: $V$, each $O(\log V)$ 
- decrease priority: $E$, each $O(\log V)$

总共 $O(V\log V+E\log V)$

- 一般会有 $E>V$，故时间复杂度为 $O(E\log V)$ 

| Problem            | Solution | Time         | Space       |
| ------------------ | -------- | ------------ | ----------- |
| Shortest Path Tree | Dijkstra | $O(E\log V)$ | $\Theta(V)$ |

### SPT on DAG

若已知图为有向无环图(Directed Acyclic Graph)，有一个更简单的方法：按照拓扑顺序访问每个顶点，更新相连顶点的距离信息。

- 对于第一次访问的顶点，将这条边加入 SPT 中；
- 如果路径没有变短，啥都不要做；

- 如果对某个相连顶点发现了更短的路径，将之前与该顶点相连的 SPT 中的边换成当前发现的那条边。

  <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220211433_image-20231220211433799.png" alt="image-20231220211433799" style="zoom: 67%;" />

  <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220211454_image-20231220211454366.png" alt="image-20231220211454366" style="zoom:67%;" />

  例如上图中，找到了到顶点 5 的更短路径 (4 $\to$ 5)，于是把原路径 (2 $\to$ 5) 从 SPT 中移除，将新路径加入。（灰粗线标记的是 SPT）

时间复杂度：$\Theta(E+V)$ 

- 拓扑排序 $\Theta(E+V)$ 
- 初始化记录数组 $\Theta(V)$ 
- 每条边(对应顶点)恰被更新一次距离信息，$\Theta(E)$ 



## A* Algorithm

只有一个目标，不需要找到整棵 SPT 树。

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220213019_image-20231220213019893.png" alt="image-20231220213019893" style="zoom:33%;" /> Dijkstra 的问题：朝那些没必要的方向

> 可视化网址：https://qiao.github.io/PathFinding.js/visual/

解决方法：不用距 source 的距离 $h_{source}(v)$ 作为访问的先后顺序，而用 $h_{source}(v)+h(v)$ 来排序，其中 $h(v)$ 为估计的从 $v$ 到 target 的距离。

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220213054_image-20231220213054382.png" alt="image-20231220213054382" style="zoom: 50%;" /> A* 算法：使之总体朝趋于 target 的方向走

### Admissible

若对任意顶点 $v$，估计距离 $h(v)$ 比实际距离 $h^*(v)$ 小，则称这一估计是 admissible 的。

**A* (Tree Search)** yields the shortest path if the heuristic is **admissible**. (充分条件)

### Consistent

We can optimize A* by “marking” any vertex that has been visited (i.e. dequeued from the PQ), and **never enqueuing such vertices again**.

This optimized version of A* is called “**A* Graph Search**”.

- Note: Vertices are marked only when dequeued, not when they are enqueued.

若对任意两个顶点 $v_1,v_2$，恒有 $h(v_1)\le h(v_2)+\operatorname{dist}(v_1,v_2)$（即不会估计出“绕远路反而更近”），则称这一估计是 consistent 的。

- All consistent heuristics are admissible.

**A* Graph Search** yields the shortest path if the heuristic is **consistent**. (充分条件)

### Summary

![image-20231220214407640](https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220214407_image-20231220214407640.png)



## Minimum Spanning Tree

（重要观察）最小生成树的性质：含 $V-1$ 条边 + Cut Property

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220214731_image-20231220214731519.png" alt="image-20231220214731519" style="zoom:67%;" />

- 以下均假设边权互不相同

因此，只要能找到一个算法满足以上性质，它就是一个正确的算法。

### Prim's Algorithm

从任一顶点开始，用优先级队列 PQ 维护与 MST 相连的顶点，每次加入距离最小的顶点 (mark the edge black)，直到把每个点都加入。

- 满足 cut property 的证明：

  <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220215451_image-20231220215451662.png" alt="image-20231220215451662" style="zoom:67%;" />

实现和 Dijkstra 很像，时间复杂度也一样 $O(V\log V+E\log V)$

- 一般会有 $E>V$，故时间复杂度为 $O(E\log V)$ 

### Kruskal's Algorithm

和 Prim 相比，加边而不是加点。

将边按边权排序，每次加入最小的边 (mark gray $\to$ black)，除非这样做产生了环 (用 DisjointSet 判断)，直到加入了 $V-1$ 条边。

- 满足 cut property 的证明：

  <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231220220147_image-20231220220147408.png" alt="image-20231220220147408" style="zoom:67%;" />

时间复杂度：$O(E\log E)$ 

- 排序 $O(E\log E)$ 
- iterate through the edges $O(E)$ 
- DisjointSet
  - union: $O(V)$, 每次 $O(\log^*V)$，共 $O(V\log^*V)$ 
  - connected: $O(E)$, 每次 $O(\log^*V)$，共 $O(E\log^*V)$ 

若事先已排序好：$O(E\log^*V)$ 

| Problem               | Solution | Time         |
| --------------------- | -------- | ------------ |
| Minimum Spanning Tree | Prim     | $O(E\log V)$ |
|                       | Kruskal  | $O(E\log E)$ |



