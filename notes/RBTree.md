# Red-Black Tree

> 免责声明：其实我并不知道我写的 [RBTree](./RBTree.java) 对不对，虽然的确写了 [test](RBTreeTest.java) 对拍。

以下均为分析 LLRB (Left-Leaning Red Black Tree)。红色节点只能是左儿子。

**从 2-3 tree 的视角去理解。**

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213210209_image-20231213210202270.png" alt="image-20231213210202270"  />

思考 `insert(put)` 的实现时，对应一棵 2-3 tree；

思考 `delete ` 的实现时，从上往下时对应一棵暂时的 2-3-4 tree（即允许 4-node 的存在），从下往上时将 4-node 修正（为 2-3 tree）。

## Insert(Put)

**插入节点时，用 `RED`**，因为对应在 2-3 tree 中只是简单的增加节点大小。

### Right-Insert

插入到右边，没有左儿子。对应的 2-3 tree 仍是一个节点。由于不允许有红色的右儿子，需要进行左旋。

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213210720_image-20231213210720718.png" alt="image-20231213210720718" style="zoom: 67%;" />

```java
rotateLeft(E);
```

<div style="display:flex">
    <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213210955_image-20231213210955898.png" alt="Image 1" style="width:45%; margin-left:5%;">
    <div style="width:5%;"></div>
    <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213211020_image-20231213211020762.png" alt="Image 2" style="width:45%; margin-right:5%;">
</div>

### Two Red Children

插入到右边，已经有个红色的左儿子。2-3 tree 会将这个 4-node split，两个儿子都变成了 2-node，颜色变为黑色。因此我们只需要简单 flip color 即可。

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213211725_image-20231213211725700.png" alt="image-20231213211725700" style="zoom:67%;" />

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213211733_image-20231213211733455.png" alt="image-20231213211733455" style="zoom:67%;" />

```java
private void flipColor(Node p) {
    p.left.color = !p.left.color;
    p.right.color = !p.right.color;
    p.color = !p.color;
}
```

注意，虽然在这里应该是简单的 `p.left.color = BLACK, p.right.color = BLACK, p.color = RED` 的行为，但随后会看到，以上的 implement 同样适用于 `delete` 时的相似情况。

### Two Reds-in-a-Row

连着两个红色，对应到 2-3 tree 中仍是与上一种情况相同的 4-node。因此只需要先右旋至上一情形，再 flip color 即可。

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213212205_image-20231213212205909.png" alt="image-20231213212205909" style="zoom:67%;" />

### General

总和起来，插入一个节点时，对某个节点的修正跑不出以上方法的一套组合技。

例如如下情形，插入 E 后(这一级当然不需要修正)，先对上一级的 A 进行左旋(变成了 A )，这一级就修正完成了；再对上一级的 F 进行右旋(变成了 E) + flip color，这一级也修正完成了；继续往上 ...

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213212553_image-20231213212553095.png" alt="image-20231213212553095" style="zoom:67%;" />

因此定义方法 `fixUp` 如下：

```java
private Node fixUp(Node p) {
    if (isRed(p.right)) {
        p = rotateLeft(p);
    }
    if (isRed(p.left) && isRed(p.left.left)) {
        p = rotateRight(p);
    }
    if (isRed(p.left) && isRed(p.right)) {
        flipColor(p);
    }
    return p;
}
```

### Implement

如上所述，插入节点时，无论任何情形，只需**递归地（从下往上）**对路径上的节点进行修正即可。而这递归顺序恰恰对应我们往下搜索插入点的过程。因此只需在 `return` 前调用 `fixUp`。

```java
public void put(K key, V val) {
    root = putHelper(key, val, root);
    root.color = BLACK; // 重要！
}

private Node putHelper(K key, V val, Node p) {
    if (p == null) {
        size += 1;
        return new Node(key, val, RED);
    }

    int cmp = key.compareTo(p.key);
    if (cmp == 0) {
        p.val = val;
    } else if (cmp < 0) {
        p.left = putHelper(key, val, p.left);
    } else {
        p.right = putHelper(key, val, p.right);
    }

    return fixUp(p);
}
```

注意最后把根节点染黑。

## Delete

### Delete the minimum

和 BST 的 `delete` 一样，先实现 `delMin` 是有用的且容易入手的。

#### Delete-the-Minimum in 2-3 tree

要删掉的最小的 key 在最底端（左下角），如果那是一个 3-node，直接把这个 key 删除即可。就怕是一个 2-node，直接删除会留下一个空节点，这会违背 perfect balance condition。

**想法：在从上往下搜索最小 key 时，时刻保持当前节点不为 2-node**（可以是 3-node；也可以是一个暂时的 4-node，这可以在之后递归的从下往上时修正掉）。

- **root**
  
  - 如果左儿子是 3-node，啥都不需要干
  - 如果左右均为 2-node，将这 3 个 node 合并为一个 4-node
  - 如果左儿子是 2-node，最近的兄弟是 3-node，可以从兄弟那借一个过来
  
  <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213215049_image-20231213215049130.png" alt="image-20231213215049130" style="zoom: 80%;" />
  
- **on the way down**

  - 如果左儿子是 3-node，啥都不需要干
  - 如果左儿子是 2-node，最近的兄弟是 3-node，可以从兄弟那借一个过来
  - 如果左右均为 2-node，将两个儿子和父亲的最小的 key (就是夹在他俩中间的那个)合并为一个 4-node，父亲相应 3-node $\to$ 2-node 或 4-node $\to$ 3-node，反正不会死

  <img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213215355_image-20231213215355756.png" alt="image-20231213215355756" style="zoom:80%;" />

- **bottom**

  - 肯定是一个 3-node 或 4-node，直接删掉最小键即可

- **on the way up**

  - 调用 `insert` 中的那个 `fixUp` 就可以修正 4-node，想想这是为什么？（这时候便是需要上面说的那个 generalized 的 flip color）

#### deleteMin in RBTree

```java
private Node deleteMinHelper(Node p) { 
    // key: 在下降的过程中保持当前节点至少是 3-node
    if (p.left == null) {
        // 到最底下了，return null 就是把最小的 key 删除了
        return null;
    }
    if (!isRed(p.left) && !isRed(p.left.left)) { 
        // 如果左儿子是 2-node，需要调整为 3-node
        p = moveRedLeft(p);
    }
    p.left = deleteMinHelper(p.left);
    return fixUp(p); // 递归地修正 4-nodes
}

private Node moveRedLeft(Node p) {
    flipColor(p); // (*)
    if (isRed(p.right.left)) { 
        // 左儿子的临近兄弟至少是 3-node，则从中拿一个过来
        p.right = rotateRight(p.right);
        p = rotateLeft(p);
        flipColor(p);
    } // 不然两个儿子都是 2-node，什么都不要干，
      // 把两个儿子和父亲合并成一个 4-node 就行了，这在 * 中已经完成了
    return p;
}
```

将上面的 2-3 tree 的变换对应到 RBTree 的旋转等操作，应该能推出 `moveRedLeft` 这个方法，它对应 on the way down 时的调整，两种调整统一在一起实现了（见以上注释）。

<img src="https://cdn.jsdelivr.net/gh/A-sock-puppet/imgbed2@main/img/2023/12/20231213221503_image-20231213221503469.png" alt="image-20231213221503469" style="zoom: 80%;" />

能够验证 `moveRedLeft` 是对的，但说实话我实在想不出这样实现 ...

### Delete

总体思路：如果要删除的 key 在最底下，直接删去(当然要保证当前节点至少为 3-node)；如果在中间，将其与右子树的最小 key 交换，然后调用 `delMin` 删除右子树的最小 key (此时即为要删除的 key 了)。最后递归地修正 4-node。

同样在向下找的过程中保证当前节点至少为 3-node。向左找时调用 `moveRedLeft`，向右找时调用一个类似的 `moveRedRight`：

```java
private Node moveRedRight(Node p) {
    flipColor(p);
    if (isRed(p.left.left)) {
        p = rotateRight(p);
        flipColor(p);
    }
    return p;
}
```

此时对 root 需要考虑上述 “Delete-the-Minimum in 2-3 tree” 中的初始调整：

```java
public V delete(K key) {
    V val = get(key);
    if (val == null) {
        return null;
    }

    if (!isRed(root.left) && !isRed(root.right)) {             
        root.color = RED;
    } // 注意！若 root 的左右儿子均为 2-node，
      // 则要先将 root 视为一个从 3-node/4-node 下来的节点
    root = deleteHelper(key, root);
    size -= 1;
    if (size != 0) {
        root.color = BLACK;
    }
    return val;
}
```

`deleteHelper` 中，仅分出向左搜索和其它情况两类(而不是向左搜索/当前节点/向右搜索)，我想可能部分原因在以下我标着“微妙”的地方。而“其它情况”实现其实有点复杂难想，这是我第二个并没有完全搞懂的地方。

```java
    private Node deleteHelper(K key, Node p) {
        if (key.compareTo(p.key) < 0) { // 向左搜索
            if (!isRed(p.left) && ! isRed(p.left.left)) {
                p = moveRedLeft(p); 
                // 保证可以向左找下去（保持当前节点至少是 3-node）
            }
            p.left = deleteHelper(key, p.left);
        } 
        else { // 其它情况
            if (isRed(p.left)) { 
                // 微妙的地方，保证当前节点的左儿子不是红色的，
                // 这样在 moveRedRight 中才不会出现一些奇怪的颜色错误
                // 类比上面 put 的过程，并没有这句话，
                // 那是因为 Left-Lean RBTree 的右儿子不可能是红色的
                p = rotateRight(p);
            }
            if (key.compareTo(p.key) == 0 && p.right == null) { 
                // 节点在最底下
                return null;
            } // 以下，则节点不是在最底下，要继续往下找
            if (!isRed(p.right) && !isRed(p.right.left)) { 
                // 保证可以向右找下去
                p = moveRedRight(p);
            }
            if (key.compareTo(p.key) == 0) { // 节点在中间
                p.key = min(p.right);
                p.val = getHelper(min(p.right), p.right);
                p.right = deleteMinHelper(p.right);
            } else { // 不然，递归地在右子树中找
                p.right = deleteHelper(key, p.right);
            }
        }
        return fixUp(p); // 递归地修正 4-nodes
    }
```

## [Implement](./RBTree.java)

