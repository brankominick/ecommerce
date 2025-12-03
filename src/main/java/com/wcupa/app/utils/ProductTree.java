package com.wcupa.app.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

import com.wcupa.app.domain.core.Product;

// AVL Tree implementation
public class ProductTree {

    private TreeNode<Product> root;
    private Comparator<Product> comparator;

    public ProductTree(Comparator<Product> comparator) {
        this.comparator = comparator;
    }

    public ProductTree() {
        this.comparator = Comparator.comparingDouble(Product::getPrice);
    }

    public int getHeight(TreeNode<Product> node) {
        int height;
        if (node == null) {
            height = 0;
        } else {
            height = node.height;
        }
        return height;
    }

    public int getMax(int x, int y) {
        return (x > y) ? x : y;
    }

    public TreeNode<Product> getRoot() {
        return this.root;
    }

    public int getBalance(TreeNode<Product> node) {
        int result;
        if (node == null) {
            result = 0;
        } else {
            result = getHeight(node.left) - getHeight(node.right);
        }
        return result;
    }

    public TreeNode<Product> rotateRight(TreeNode<Product> node) {
        TreeNode<Product> leftChild = node.left;
        TreeNode<Product> temp = leftChild.right;

        leftChild.right = node;
        node.left = temp;
        
        node.height = getMax(getHeight(node.left), getHeight(node.right)) + 1;
        leftChild.height = getMax(getHeight(leftChild.left), getHeight(leftChild.right)) + 1;

        return leftChild;
    }

    public TreeNode<Product> rotateLeft(TreeNode<Product> node) {
        TreeNode<Product> rightChild = node.right;
        TreeNode<Product> temp = rightChild.left;

        rightChild.left = node;
        node.right = temp;
        
        node.height = getMax(getHeight(node.left), getHeight(node.right)) + 1;
        rightChild.height = getMax(getHeight(rightChild.left), getHeight(rightChild.right)) + 1;

        return rightChild;
    }

    private TreeNode<Product> _insert(TreeNode<Product> node, Product key) {
        TreeNode<Product> result = null;
        // Base case - do insertion
        if (node == null) {
            return new TreeNode<>(key);
        }

        // Determine which subtree to insert into
        int cmp = comparator.compare(key, node.data);
        if (cmp < 0) {
            node.left = _insert(node.left, key);
        } else {
            node.right = _insert(node.right, key);
        }

        // Update height and get balance
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 && comparator.compare(key, node.left.data) < 0) {
            return rotateRight(node);
        }
        // Right Right
        if (balance < -1 && comparator.compare(key, node.right.data) > 0) {
            return rotateLeft(node);
        }
        // Left Right
        if (balance > 1 && comparator.compare(key, node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // Right Left
        if (balance < -1 && comparator.compare(key, node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void insert(Product key) {
        root = _insert(root, key);
    }

    public ArrayList<Product> preOrder(TreeNode<Product> root) {
        ArrayList<Product> result = new ArrayList<>();
        Stack<TreeNode<Product>> stack = new Stack<>();
        if (root != null) {
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode<Product> node = stack.pop();
                result.add(node.data);

                if (node.right != null) {
                    stack.push(node.right);
                }
                if (node.left != null) {
                    stack.push(node.left);
                }
            }
        }
        return result;
    }

    public ArrayList<Product> inOrder(TreeNode<Product> root) {
        ArrayList<Product> result = new ArrayList<>();
        Stack<TreeNode<Product>> stack = new Stack<>();
        TreeNode<Product> current = root;

       while (current != null || !stack.isEmpty()) {
            while (current != null) {
            stack.push(current);
            current = current.left;
            }

            current = stack.pop();
            result.add(current.data);

            current = current.right;
        }
        return result;
    }

    public ArrayList<Product> postOrder(TreeNode<Product> root) {
        ArrayList<Product> result = new ArrayList<>();
        Stack<TreeNode<Product>> stack = new Stack<>();
        TreeNode<Product> current = root;

       while (current != null || !stack.isEmpty()) {
            while (current != null) {
            stack.push(current);
            current = current.right;
            }

            current = stack.pop();
            result.add(current.data);

            current = current.left;
        }
        return result;
    }
    
}
