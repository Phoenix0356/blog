package com.phoenix.base.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.base.model.entity.ArticleMessage;
import com.phoenix.base.model.vo.ArticleMessageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<ArticleMessage> {
    List<ArticleMessageVO> getMessageList(String receiverId);

    void savePulledMessage(String receiverId,String dataStateType);
}
