package org.nb.petHome.service.impl;

import org.nb.petHome.entity.Department;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.mapper.DepartmentMapper;
import org.nb.petHome.mapper.EmployeeMapper;
import org.nb.petHome.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Service
public class EmployeeService implements IEmployeeService {

    private DepartmentMapper departmentMapper;

    private EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeService(DepartmentMapper departmentMapper,EmployeeMapper employeeMapper){
        this.departmentMapper = departmentMapper;
        this.employeeMapper=employeeMapper;
    }

    @Transactional
    @Override
    public boolean add(Employee employee) {
        int rows=employeeMapper.add(employee);
        if (rows==0){
            return false;
        }else {
            Department department=this.departmentMapper.find(employee.getDid());
            employee.setDepartment(department);
            return true;
        }
    }


    @Transactional
    @Override
    public void remove(Long id) {
        employeeMapper.remove(id);
    }


    @Transactional
    @Override
    public void update(Employee employee) {
        employeeMapper.update(employee);
    }

    @Override
    public List<Employee> findEmployees() {
        return employeeMapper.findEmployees();
    }

    @Override
    public Employee findById(Long id) {
        return employeeMapper.findById(id);
    }

    @Override
    public Employee login(Employee employee) {
        return employeeMapper.login(employee);
    }

}
