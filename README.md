# Optimal-Algorithms-for-l1-Heavy-Hitters
Two Optimal Algorithms for l1-Heavy Hitters in Insertion Streams

### Dataset
Small Wikipedia files

### 3 Algorithms Implemeted
1. A simpler, near-optimal algorithm for (ε, φ)-List heavy hitters
2. An optimal algorithm for (ε, φ)-List heavy hitters
3. Misra-Gries

We will evaluate the space, error, time for the 3 algorithms compared to true values.

With large enough dataset, algorithm 1 and 2 will be more space-efficient, but slower than the Misra-Gries algorithm.

### True results
Python program [prepocessing.py](https://github.com/xiyinzhi/Optimal-Algorithms-for-l1-Heavy-Hitters/blob/master/pre-processing.py) for data clean and word-count.

### Codes
We implement the 3 algorithms in Java in [project](https://github.com/xiyinzhi/Optimal-Algorithms-for-l1-Heavy-Hitters/tree/master/project/src).

For example, you can use following instructions to run the code for near-optimal algorithm:

    javac NearOptimal.java

    java NearOptimal


