package com.play.playsystem.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.play.playsystem.post.domain.entity.Favorite;
import com.play.playsystem.post.domain.vo.FavoriteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
    @Select("SELECT created_id FROM t_favorite WHERE id = #{favoriteId}")
    Long getCreatedIdById(Long favoriteId);

    @Select("SELECT opened FROM t_favorite WHERE id = #{favoriteId}")
    Boolean getOpenedById(Long favoriteId);

    List<FavoriteVo> getFavoritesByUserId(Long userId);

    List<FavoriteVo> getOpenedFavoritesByUserId(Long userId);
}
