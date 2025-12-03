package com.wcupa.app.utils;

public class TreeNode<T> {
    public T data;
    public int height;
    public TreeNode<T> left, right;

    public TreeNode(T data) {
        this.data = data;
        this.height = 1;
    }

    public TreeNode(T data, TreeNode<T> left, TreeNode<T> right) {
        this.data = data;
        this.height = 1;
        this.left = left;
        this.right = right;
    }
}
