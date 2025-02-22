package com.xiaohui.pocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohui.pocket.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;


/**
 * 系统日志数据访问层
 *
 * @author Ray
 * @since 2.10.0
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {
}




