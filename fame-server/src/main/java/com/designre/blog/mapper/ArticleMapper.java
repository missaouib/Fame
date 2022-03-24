package com.designre.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.designre.blog.model.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


public interface ArticleMapper extends BaseMapper<Article> {

    @Update("update article set hits = hits + #{increaseHits} where id = #{id} and deleted = 0")
    int increaseHits(@Param("id") Integer id, @Param("increaseHits") Integer increaseHits);
}
