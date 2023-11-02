package org.nb.petHome.service;

import org.nb.petHome.common.DepartmentQuery;
import org.nb.petHome.entity.Department;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
public interface IDepartmentService {
    void add(Department d);
    void remove(Long id);
    void update(Department d);
    Department find(Long id);
    List<Department> findAll();
    Long queryCount();
    List<Department> findDepartmentsByPage(DepartmentQuery query);

    List<Department> getDepartmentTreeData();
}
