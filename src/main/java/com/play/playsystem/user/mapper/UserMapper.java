package com.play.playsystem.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.user.domain.entity.User;
import com.play.playsystem.user.domain.vo.UserDetailsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    UserDetailsVo getUserDetailsById(Long id);

    @Select("select id from t_user where username = #{username}")
    Long getIdByUsername(String username);
}
