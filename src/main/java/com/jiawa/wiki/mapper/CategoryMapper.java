package com.jiawa.wiki.mapper;

import com.jiawa.wiki.domain.Category;
import com.jiawa.wiki.domain.CategoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    long countByExample(CategoryExample example);

    static int deleteByExample(CategoryExample example){
        return 1;
    }

    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}