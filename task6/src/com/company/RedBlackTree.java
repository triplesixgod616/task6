package com.company;

public class RedBlackTree<T extends Comparable<? super T>, V> {
    @FunctionalInterface
    interface Visitor<T, V> {
        void visit(Node<T, V> node);
    }

    static class Node<T, V> {
        public T key;
        public V value;
        public boolean isBlack;
        public Node<T, V> parent;
        public Node<T, V> left;
        public Node<T, V> right;

        public Node(T key, V value, boolean isBlack, Node<T, V> parent, Node<T, V> left, Node<T, V> right) {
            this.key = key;
            this.value = value;
            this.isBlack = isBlack;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public Node(T key, V value, Node<T, V> parent) {
            this(key, value, false, null, null, parent);
        }

        public Node(T key, V value) {
            this(key, value, null);
        }

        public void setLeft(Node<T, V> node) {
            left = node;
            if(right != null) left.parent = this;
        }

        public void setRight(Node<T, V> node) {
            right = node;
            if(right != null) right.parent = this;
        }

        public void setBlack() {
            isBlack = true;
        }

        public void setRed() {
            isBlack = false;
        }

        public V getValue() {
            return value;
        }

        public T getKey() {
            return key;
        }
    }

    private Node<T, V> root = null;
    int size = 0;

    public Node<T, V> getRoot() {
        return root;
    }

    public int size() {
        return size;
    }

    public V put(T key, V value) {
        if (root == null) {
            setRoot(new Node<>(key, value));
            size++;
            correctAfterAdd(root);
            return null;
        }

        Node<T, V> node = root;

        while (true) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                V oldValue = node.value;
                node.key = key;
                node.value = value;
                correctAfterAdd(node);
                return oldValue;
            } else if (cmp < 0) {
                if (node.left == null) {
                    node.setLeft(new Node<>(key, value));
                    size++;
                    correctAfterAdd(node.left);
                    return null;
                }
                node = node.left;
            } else {
                if (node.right == null) {
                    node.setRight(new Node<>(key, value));
                    size++;
                    correctAfterAdd(node.right);
                    return null;
                }
                node = node.right;
            }
        }
    }

    public V remove(T key) {
        Node<T, V> node = getNode(key);
        if (node == null) {
            return null;
        }

        V oldValue = node.value;
        if (node.left != null && node.right != null) {
            Node<T, V> nextValueNode = findMaxNode(node.left);
            node.value = nextValueNode.value;
            node = nextValueNode;
        }

        Node<T, V> child = (node.left != null) ? node.left : node.right;
        if (child != null) {
            if (node == root) {
                setRoot(child);
                root.setBlack();
            } else if (node.parent.left == node) {
                node.parent.setLeft(child);
            } else {
                node.parent.setRight(child);
            }
            if (isBlack(node)) {
                correctAfterRemove(child);
            }
        } else if (node == root) {
            root = null;
        } else {
            if (isBlack(node)) {
                correctAfterRemove(node);
            }
            if (node.parent != null) {
                if (node.parent.left == node) {
                    node.parent.left = null;
                } else if (node.parent.right == node) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
        size--;
        return oldValue;
    }

    private Node<T, V> getNode(T key) {
        Node<T, V> node = root;

        while (true) {
            if (node == null) return null;

            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
    }

    public V getValue(T key) {
        Node<T, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    private void correctAfterAdd(Node<T, V> node) {
        if(node.parent == null) {
            node.setBlack();
            return;
        }
        if(isBlack(node.parent)) {
            return;
        }
        Node<T, V> uncle = uncleOf(node);
        if(uncle != null && isRed(uncle)) {
            node.parent.setBlack();
            uncle.setBlack();
            Node<T, V> grandparent = grandparentOf(node);
            grandparent.setRed();
            correctAfterAdd(grandparent);
            return;
        }

        Node<T, V> grandparent = grandparentOf(node);
        if(node == node.parent.right && node.parent == grandparent.left) {
            leftRotate(node.parent);
            node = node.left;
        } else if(node == node.parent.left && node.parent == grandparent.right) {
            rightRotate(node.parent);
            node = node.right;
        }

        grandparent = grandparentOf(node);
        node.parent.setBlack();
        grandparent.setRed();
        if(node == node.parent.left && node.parent == grandparent.left) {
            rightRotate(grandparent);
        } else {
            leftRotate(grandparent);
        }
    }

    private void correctAfterRemove(Node<T, V> node) {
        while (node != root && isBlack(node)) {
            if (node == node.parent.left) {
                Node<T, V> sibling = siblingOf(node);
                if (isRed(sibling)) {
                    sibling.setBlack();
                    node.parent.setRed();
                    leftRotate(node.parent);
                    sibling = siblingOf(node.parent);
                }
                if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    sibling.setRed();
                    node = node.parent;
                } else {
                    if (isBlack(sibling.right)) {
                        sibling.left.setBlack();
                        sibling.setRed();
                        rightRotate(sibling);
                        sibling = node.parent.right;
                    }
                    sibling.isBlack = node.parent.isBlack;
                    node.parent.setBlack();
                    sibling.right.setBlack();
                    leftRotate(node.parent);
                    node = root;
                }
            } else {
                Node<T, V> sibling = node.parent.left;

                if (isRed(sibling)) {
                    sibling.setBlack();
                    node.parent.setRed();
                    rightRotate(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling.left.isBlack && isBlack(sibling.right)) {
                    sibling.setRed();
                    node = node.parent;
                } else {
                    if (isBlack(sibling.left)) {
                        sibling.right.setBlack();
                        sibling.setRed();
                        leftRotate(sibling);
                        sibling = node.parent.left;
                    }
                    sibling.isBlack = node.parent.isBlack;
                    node.parent.setBlack();
                    sibling.left.setBlack();
                    rightRotate(node.parent);
                    node = root;
                }
            }
        }
        node.setBlack();
    }

    private void leftRotate(Node<T, V> node) {
        Node<T, V> pivot = node.right;

        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.setLeft(pivot);
            } else {
                node.parent.setRight(pivot);
            }
        }

        node.setRight(pivot.left);
        if (pivot.left != null) {
            pivot.left.parent = node;
        }

        node.parent = pivot;
        pivot.setLeft(node);
        if(node == root) setRoot(pivot);
    }

    private void rightRotate(Node<T, V> node) {
        Node<T, V> pivot = node.left;

        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.setLeft(pivot);
            } else {
                node.parent.setRight(pivot);
            }
        }

        node.setLeft(pivot.right);
        if (pivot.right != null) {
            pivot.right.parent = node;
        }

        node.parent = pivot;
        pivot.setRight(node);
        if(node == root) setRoot(pivot);
    }

    private Node<T, V> findMaxNode(Node<T, V> node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private void setRoot(Node<T, V> node) {
        root = node;
        if (node != null) {
            node.parent = null;
        }
    }

    private Node<T, V> uncleOf(Node<T, V> node) {
        return siblingOf(node.parent);
    }

    private Node<T, V> grandparentOf(Node<T, V> node) {
        return (node == null || node.parent == null) ? null : node.parent.parent;
    }

    private Node<T, V> siblingOf(Node<T, V> node) {
        return (node == null || node.parent == null) ? null : ((node == node.parent.left) ? node.parent.right : node.parent.left);
    }

    public boolean isBlack(Node<T, V> node) {
        return node == null || node.isBlack;
    }

    public boolean isRed(Node<T, V> node) {
        return node != null && !node.isBlack;
    }


    public void preOrderVisit(Node<T, V> node, Visitor<T, V> visitor) {
        if (node == null) {
            return;
        }
        visitor.visit(node);
        preOrderVisit(node.left, visitor);
        preOrderVisit(node.right, visitor);
    }
}
