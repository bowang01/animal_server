package com.example.mapper;

import com.example.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper interface
 * </p>
 *
 * @author

 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select * from notice order by id desc limit 5 ")
    List<Notice> limit(int i);
}
