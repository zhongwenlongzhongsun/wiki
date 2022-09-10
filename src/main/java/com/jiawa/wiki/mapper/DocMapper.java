package com.jiawa.wiki.mapper;

import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocMapper {
    long countByExample(DocExample example);

    static int deleteByExample(DocExample example) {
        return 1;
    }

    int deleteByPrimaryKey(Long id);

    int insert(Doc record);

    int insertSelective(Doc record);

    List<Doc> selectByExample(DocExample example);

    Doc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByExample(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByPrimaryKeySelective(Doc record);

    int updateByPrimaryKey(Doc record);
}