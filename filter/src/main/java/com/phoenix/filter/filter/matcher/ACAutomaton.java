package com.phoenix.filter.filter.matcher;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Component
public class ACAutomaton {
    private final Node root;
    private final ReentrantLock reentrantLock;
    public ACAutomaton(){
        reentrantLock = new ReentrantLock();
        root = new Node();
        root.val = null;
    }

    public void init(List<String> wordList){
        //保证线程安全
        reentrantLock.lock();
        try {
            wordList.forEach(s->{
                if(s.length()<=1) return;
                addNode(s);
            });
            linkAllRematchNodes();
        }finally {
            reentrantLock.unlock();
        }
    }

    public int getSize(){
        int len = 0;
        Queue<Node> nodeQueue = new ArrayDeque<>();
        nodeQueue.offer(root);
        while (!nodeQueue.isEmpty()){
            Node cur = nodeQueue.poll();
            len++;
            for (Node child:cur.childrenNodes.values()){
                nodeQueue.offer(child);
            }
        }
        return len;
    }
    public void addNode(String word){
        char[] chars = word.toCharArray();
        int len = chars.length;
        //初始化第一个子节点
        Node child = append(root,chars[0], len == 1);
        //整个词加入树
        for (int i = 1; i < chars.length; i++) {
            child = append(child,chars[i],i == len-1);
        }
    }

    public void linkAllRematchNodes(){
        //bfs算法匹配失配节点
        //初始化第一层节点rematchNode,并加入队列
        Queue<Node> queue = new ArrayDeque<>();
        for (Node node:root.childrenNodes.values()){
            node.reMatchNode = root;
            queue.offer(node);
        }
        while (!queue.isEmpty()){
            Node curNode = queue.poll();
            linkRematchNode(curNode);
            for (Node childNode:curNode.childrenNodes.values()){
                queue.offer(childNode);
            }
        }
    }

    private void linkRematchNode(Node node){
        //第一层节点直接返回
        if (node.parentNode == root) return;

        //获取父节点的reMatchNode
        Node prevRematchNode = node.parentNode.reMatchNode;
        //如果父节点的失配节点的子节点中有与当前节点值相同的，则设为当前节点的失配节点
        if (prevRematchNode.childrenNodes.containsKey(node.val)) {
            node.reMatchNode = prevRematchNode.getChild(node.val);
            return;
        }
        //不断查找失配节点的失配节点的子节点
        while (prevRematchNode != root) {
            prevRematchNode = prevRematchNode.reMatchNode;
            if (prevRematchNode.childrenNodes.containsKey(node.val)) {
                node.reMatchNode = prevRematchNode.getChild(node.val);
                return;
            }
        }
        //如果一直找到root节点还未找到，则把失配节点设为root
        if (node.reMatchNode == null) node.reMatchNode = root;
    }

    //构建前缀枝条节点
    private Node append(Node curNode,char newVal,boolean isLast){
        //如果当前节点已经存在对应子节点，则不需要构建，直接返回子节点
        if (curNode.childrenNodes.containsKey(newVal)){
            return curNode.getChild(newVal);
        }
        Node child = new Node(curNode,newVal,isLast);
        //子节点加入树
        curNode.addChild(newVal,child);
        return child;
    }

    //前缀树节点
    public static class Node {
        Node parentNode;
        Character val;

        int depth;
        boolean isLast;

        //单个字符对应一节点
        Node reMatchNode;
        HashMap<Character,Node> childrenNodes;

        //初始化根节点
        public Node() {
            this.parentNode = null;
            this.val = null;
            this.depth = 0;
            this.isLast = false;
            this.reMatchNode = null;
            this.childrenNodes = new HashMap<>();
        }

        //节点入树时初始化使用，从父节点处初始化深度
        public Node(Node parentNode,Character val,boolean isLast) {
            this.parentNode = parentNode;
            this.val = val;
            this.depth = parentNode.depth +1;
            this.isLast = isLast;
            this.reMatchNode = null;
            this.childrenNodes = new HashMap<>();
        }
        public Node getChild(Character character){
            return childrenNodes.get(character);
        }
        public void addChild(Character character,Node childNode){
            childrenNodes.put(character,childNode);
        }
    }
}
