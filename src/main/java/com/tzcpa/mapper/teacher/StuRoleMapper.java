package com.tzcpa.mapper.teacher;

import com.tzcpa.model.teacher.StuRole;

import java.util.List;

public interface StuRoleMapper {

    int insertStuRole(StuRole stuRole);

    int updateStuRole(StuRole stuRole);

    List<StuRole> selectStuRole(StuRole stuRole);

}
