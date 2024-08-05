package com.phoenix.blog.model.pojo;

import com.phoenix.blog.exceptions.serverException.LockPoolException;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

//Todo:优化掉synchronized
public class LinkedConcurrentMap<T,V> {
    static final int DEFAULT_CAPACITY = 20;
    private final ConcurrentHashMap<T,Element<T,V>> concurrentHashMap;
    private final DoubleLinkedList<T,V> doubleLinkedList;
    int capacity;

    public LinkedConcurrentMap(){
        concurrentHashMap = new ConcurrentHashMap<>();
        doubleLinkedList = new DoubleLinkedList<>();
        capacity = DEFAULT_CAPACITY;
    }

    public LinkedConcurrentMap(int capacity){
        concurrentHashMap = new ConcurrentHashMap<>();
        doubleLinkedList = new DoubleLinkedList<>();
        this.capacity = capacity;
    }

    public synchronized V put(T key,Class<V> clazz){
        Element<T,V> element;
        try {
            V instance = clazz.getDeclaredConstructor().newInstance();
            if (concurrentHashMap.containsKey(key)) {
                element = concurrentHashMap.get(key);
                doubleLinkedList.delete(element);
                doubleLinkedList.addLast(element);
                return instance;
            }

            element = new Element<>(key, instance);
            concurrentHashMap.put(key, element);
            doubleLinkedList.addLast(element);

            dropIfExceedCapacity();
        }catch (LockPoolException | NoSuchMethodException |InvocationTargetException
                | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        return concurrentHashMap.get(key).val;
    }

    public synchronized V getIfAbsent(T key,Class<V> clazz) {
        V v = get(key);
        if (v!=null){
            return v;
        }
        v=put(key,clazz);
        return v;
    }

    public synchronized V get(T key){
        try {
            if (concurrentHashMap.containsKey(key)) {
                Element<T, V> element = concurrentHashMap.get(key);
                doubleLinkedList.delete(element);
                doubleLinkedList.addLast(element);
                return element.val;
            }
        }catch (LockPoolException lockPoolException){
            lockPoolException.printStackTrace();
        }
        return null;
    }

    public synchronized int size(){
        return doubleLinkedList.size();
    }

    private void dropIfExceedCapacity() throws LockPoolException {
        if (doubleLinkedList.size() >= capacity  ) {
            T key = doubleLinkedList.deleteFirst();
            concurrentHashMap.remove(key);
        }
    }

    static class DoubleLinkedList<T,V>{

        //占位节点
        Element<T,V> fakeHead;
        Element<T,V> fakeTail;
        private int size;

        public DoubleLinkedList(){
            fakeHead = new Element<>();
            fakeTail = new Element<>();
            size = 0;

            fakeHead.next = fakeTail;
            fakeTail.pre = fakeHead;
        }

        private void addLast(Element<T,V> element) throws LockPoolException {
            if (fakeTail !=null) {

                element.pre = fakeTail.pre;
                element.next = fakeTail;

                fakeTail.pre.next = element;
                fakeTail.pre = element;

                size++;

            }else {
                throw new LockPoolException("Can't add the value because the last node is missing");
            }
        }

        private void delete(Element<T,V> element){
            element.pre.next = element.next;
            element.next.pre = element.pre;
            size--;

        }
        private T deleteFirst() throws LockPoolException {
            if (fakeHead.next == fakeTail){
                throw new LockPoolException("Can't delete first node because the node is missing");
            }
            Element<T,V> element = fakeHead.next;
            T key = element.key;
            delete(element);
            return key;
        }

        private int size(){
            return size;
        }
    }
    static class Element<T,V> {
        T key;
        V val;
        Element<T,V> next;
        Element<T,V> pre;
        public Element(){}
        public Element(T key,V val){
            this.key = key;
            this.val = val;
        }
        public Element(T key,V val, Element<T,V> pre,Element<T,V> next){
            this.key = key;
            this.val = val;
            this.pre = pre;
            this.next = next;
        }
    }
}
