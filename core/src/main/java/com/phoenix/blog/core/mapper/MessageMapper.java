package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.ArticleMessage;
import com.phoenix.blog.model.vo.ArticleMessageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<ArticleMessage> {
    List<ArticleMessageVO> getMessageList(String receiverId);

    void savePulledMessage(String receiverId,String messageType);
}
