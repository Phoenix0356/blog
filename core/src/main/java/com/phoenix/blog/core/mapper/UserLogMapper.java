package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.UserLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {

}
