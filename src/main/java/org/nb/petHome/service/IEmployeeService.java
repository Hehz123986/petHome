package org.nb.petHome.service;

import org.nb.petHome.entity.Employee;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
public interface IEmployeeService {

    boolean add(Employee employee);

    void remove(Long id);

    void update(Employee employee);

    List<Employee> findEmployees();

    Employee findById(long id);

}
