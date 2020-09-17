package com.tzcpa.model.treatment;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SandTableVO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/13 18:30
 * @Version 6.0
 **/
@Data
public class SandTableVO {

    private String time;
    private List<Map<String,Object>> buttonList;
}
