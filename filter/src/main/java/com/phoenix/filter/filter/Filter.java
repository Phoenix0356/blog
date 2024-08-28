package com.phoenix.filter.filter;

import com.phoenix.filter.filter.enumeration.MatchResult;
import com.phoenix.filter.filter.matcher.WordMatcher;
import com.phoenix.filter.util.TextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Filter {

    final FilterHelper filterHelper;
    private static final char REPLACE_CHARACTER = '*';

    //注入多例bean
    @Lookup
    protected WordMatcher generateWordMatcher(){
        return null;
    }
//    public Object filterObjectText(Object object){
//        List<Field> fieldList = filterHelper.extractStringFields(object);
//        fieldList.forEach(field -> {
//            try {
//                filterHelper.setStringFields(object,field,filterText((String) field.get(object)));
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return object;
//    }

    //Todo:对于一个节点，其可能是某个敏感词末尾，但并不说明其没有子节点，而目前逻辑会直接忽略这些子节点
    public String filterText(String text){
        WordMatcher wordMatcher = generateWordMatcher();

        StringBuilder stringBuilder = new StringBuilder();
        int startMatchingInd = 0,endMatchingInd = 0;
        char[] textChars = text.toCharArray();

        //滑动窗口
        while (endMatchingInd<text.length()){
            //字符进入匹配器尝试匹配
            int matchingResult =  wordMatcher.match(textChars[endMatchingInd++]);

            //如果加入字符导致切换了词语，需要把换词前的那部分安全字符加入stringBuilder
            if (wordMatcher.isWordChangedFlag()){
                int curWordLength = wordMatcher.getCurWordLength();
                //计算出安全字符的右边界
                int safeEndInd = endMatchingInd-curWordLength;
                stringBuilder.append(text,startMatchingInd,safeEndInd);
                //安全字符入stringBuilder后更新左窗口边界
                startMatchingInd = safeEndInd;
                wordMatcher.resetWordChangeFlag();
            }

            //匹配器正在匹配当前词语
            if (matchingResult == MatchResult.PROCESSING.getIdentifier()){
                continue;
            }
            //匹配器结束了当前词语的匹配
            else {
                //初始化状态
                wordMatcher.initState();
                //如果未匹配到，把安全字符加入stringBuilder，
                if (matchingResult == MatchResult.NOT_MATCH.getIdentifier()){
                    stringBuilder.append(text, startMatchingInd, endMatchingInd);
                }
                //如果匹配到,把等量替换字符加入stringBuilder
                else if (matchingResult > 0){
                    TextUtil.appendReplaceChar(stringBuilder,
                            REPLACE_CHARACTER,
                            endMatchingInd-startMatchingInd);
                }
            }
            //设置窗口左边界
            startMatchingInd = endMatchingInd;
        }
        //末尾安全字符加入stringBuilder
        if (startMatchingInd<endMatchingInd){
            stringBuilder.append(text, startMatchingInd, endMatchingInd);
        }
        //最后初始化匹配器状态
        wordMatcher.initState();
        return stringBuilder.toString();
    }

    public Boolean detectText(String text){
        WordMatcher wordMatcher = generateWordMatcher();
        int endMatchingInd = 0;
        char[] textChars = text.toCharArray();
        while (endMatchingInd<text.length()){
            //字符进入匹配器尝试匹配
            int matchingResult =  wordMatcher.match(textChars[endMatchingInd++]);
            if (matchingResult > 0){
                return true;
            }
        }
        wordMatcher.initState();
        return false;
    }
}
