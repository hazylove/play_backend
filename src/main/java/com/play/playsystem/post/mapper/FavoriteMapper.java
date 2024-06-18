package com.play.playsystem.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.post.domain.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
    @Select("SELECT created_id FROM t_favorite WHERE id = #{favoriteId}")
    Long getCreatedIdById(Long favoriteId);
}
