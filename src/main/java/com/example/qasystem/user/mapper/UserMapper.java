package com.example.qasystem.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.qasystem.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
