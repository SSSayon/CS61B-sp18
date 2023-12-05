# Disjoint Sets

## Analysis

以下均设有 `n` 个元素，`m` 次操作( `connected` & `union` )。

### Idea1: Quick Find

每个元素指定一个数字(或字母)，`connected` 方法直接检查两元素的数字是否相同，`union` 方法改变一个元素所在集合的所有元素的数字至另一个元素的数字。

| `construction` | `union`     | `connected` |
| -------------- | ----------- | ----------- |
| $\Theta(n)$    | $\Theta(n)$ | $\Theta(1)$ |

问题：`union` 操作慢，每次都要遍历以寻找同在一个集合的所有元素，$\Theta(n)$ 

### Idea2: Quick Union

每个元素指定父亲，父亲是自己的元素称为“根”(初始时每个元素都是根)。定义以下帮助方法，得到一个元素的根

```java
private T find(T p) {
    while (!p.equals(p.parent)) {
        p = p.parent;
    }
    return p;
}
```

`union` 方法将一个元素的根指定父亲为另一个元素的根，`connected` 方法比较两个元素的根是否相同。

问题：树可能变得很长，例如以下极端情况

```java
union(4, 3);
union(3, 2);
union(2, 1);
union(1, 0);
```

会形成一棵 $0 \leftarrow 1 \leftarrow 2 \leftarrow 3 \leftarrow 4$ 这样的树，`find` 复杂度变为 $O(n)$，`union` 和 `isConnect` 都要调用 `find`，同样达到 $O(n)$。

| `construction` | `union` | `connected` |
| -------------- | ------- | ----------- |
| $\Theta(n)$    | $O(n)$  | $O(n)$      |

### Improve idea2: Weighted Quick Union

针对树过长的问题，每次 `union` 时，将“小”的树连到“大”的树。

增加 track 衡量树的大小的指标：

- `size`: 就是每棵树的元素个数

- 或 `rank`: 每棵树高度的上界(不直接 track 树的高度是因为在下面进一步改进的方法中 `find` 会改变树的高度，仅目前这一节倒无影响)

这样，树的高度不会超过 $\log n$ 。

| `construction` | `union`     | `connected` |
| -------------- | ----------- | ----------- |
| $\Theta(n)$    | $O(\log n)$ | $O(\log n)$ |

### Improve idea3: WQU with Path Compression

 一个聪明的想法：每次 `find` 时，将路径上的元素都直接连到根上。可用递归实现如下

```java
private T find(T p) {
    if (T.equals(p.parent)) {
        return p;
    } else {
        p.parent = find(p.parent);
        return p.parent;
    }
}
```

- 额外操作的代价是很小的(同级增长)
- 每次 `find` 的时间复杂度几乎是 $O(1)$ (amortized constant time)
  - 证明 $O(\log^*n)$ 参见 [Wikipedia](https://en.wikipedia.org/wiki/Disjoint-set_data_structure) 
  - 确切的复杂度：$\Theta(\alpha(n))$，其中 $\alpha$ 是 inverse Ackermann function，“this factor is 4 or less for any *n* that can actually be written in the physical universe”

| `construction` | `union`             | `connected`         |
| -------------- | ------------------- | ------------------- |
| $\Theta(n)$    | $\Theta(\alpha(n))$ | $\Theta(\alpha(n))$ |

## Implements



