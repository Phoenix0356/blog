package com.phoenix.filter.filter.matcher;

import com.phoenix.filter.filter.enumeration.MatchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WordMatcher implements InitializingBean {

    private final ACAutomaton automaton;
    private ACAutomaton.Node preMatchingNode;
    private int matchingCount;

    @Override
    public void afterPropertiesSet(){
        initState();
    }
    public void init(List<String> wordsList){
        wordsList.forEach(automaton::addNode);
        automaton.linkRematchNodes();
    }

    public void initState(){
        matchingCount = 0;
        preMatchingNode = automaton.getRoot();
    }

//    public void resetWordChangedFlag(){
//        matchingCount--;
//        isWordChanged = false;
//    }

    public int match(Character character){
        ACAutomaton.Node curNode = preMatchingNode.getChild(character);
        //如果从上一个节点能匹配到
        if (curNode!=null){
            //加入当前字符，并更新pre节点
           matchingCount++;
           preMatchingNode = curNode;
            //如果匹配到结尾返回匹配到的数量
            if (curNode.isLast) {
                return matchingCount;
            }
            //不是结尾,返回当前状态
            return MatchResult.PROCESSING.getIdentifier();
        }

        //如果刚开始就未匹配到或者跳到root还是匹配不到，返回未匹配到
        if (preMatchingNode == automaton.getRoot()){
            return MatchResult.NOT_MATCH.getIdentifier();
        }

        //跳到失配节点，设置换词标记为true
        preMatchingNode = preMatchingNode.reMatchNode;
        //再次尝试匹配
        return match(character);
    }



//    public void bfsPrintTree(){
//        ArrayList<ACAutomaton.Node> cacheList = new ArrayList<>();
//        Queue<ACAutomaton.Node> queue = new ArrayDeque<>();
//        queue.offer(automaton.getRoot());
//        while (!queue.isEmpty()){
//            ACAutomaton.Node cur = queue.poll();
//            if (cur!= automaton.getRoot()) {
//                System.out.printf("%s，%s，%s，%s   "
//                        , cur.val, cur.parentNode.val, cur.reMatchNode.val,cur.isLast);
//            }
//            cacheList.addAll(cur.childrenNodes.values());
//            if (queue.isEmpty()&&!cacheList.isEmpty()){
//                cacheList.forEach(queue::offer);
//                System.out.println();
//                cacheList.clear();
//            }
//
//        }
//    }
}
