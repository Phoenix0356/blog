package com.phoenix.filter.filter;

import com.phoenix.filter.enumeration.MatchResult;
import com.phoenix.filter.filter.matcher.WordMatcher;
import com.phoenix.filter.util.TextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextFilter {

    final WordMatcher wordMatcher;
    private static final char REPLACE_CHARACTER = '*';
    public String filterText(String text){
        StringBuilder stringBuilder = new StringBuilder();
        int startMatchingInd = 0,endMatchingInd = 0;
        char[] textChars = text.toCharArray();

        //滑动窗口
        while (endMatchingInd<text.length()){
            //匹配敏感字符
            int matchingResult =  wordMatcher.match(textChars[endMatchingInd++]);

//            System.out.println(textChars[endMatchingInd-1]+" "+matchingResult);

            //如果匹配正在进行
            if (matchingResult == MatchResult.PROCESSING.getIdentifier()){
                continue;
            }
            //如果匹配结束，初始化匹配器状态
            else {
                wordMatcher.initState();
                //如果未匹配到，把安全字符加入stringBuilder，
                if (matchingResult == MatchResult.NOT_MATCH.getIdentifier()){
                    stringBuilder.append(text, startMatchingInd, endMatchingInd);
                }
                //如果匹配到,把等量替换字符加入stringBuilder，并清理匹配器状态
                else if (matchingResult > 0){
                    TextUtil.appendReplaceChar(stringBuilder,
                            REPLACE_CHARACTER,
                            endMatchingInd-startMatchingInd);
                }
            }
            //完成一次匹配，设置窗口左边界
            startMatchingInd = endMatchingInd;
        }
        //最后初始化匹配器状态
        wordMatcher.initState();
        return stringBuilder.toString();
    }
}
