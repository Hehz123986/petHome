package org.nb.petHome.controller;

import org.nb.petHome.common.Urls;
import org.nb.petHome.entity.Department;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.interceptor.TokenInterceptor;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.IDepartmentService;
import org.nb.petHome.service.IEmployeeService;
import org.nb.petHome.utils.MD5Util;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@RestController
public class EmployeeController {

    private IDepartmentService iDepartmentService;
    private IEmployeeService iEmployeeService;
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    public EmployeeController(IDepartmentService iDepartmentService, IEmployeeService iEmployeeService) {
        this.iDepartmentService = iDepartmentService;
        this.iEmployeeService = iEmployeeService;
    }

    @PostMapping(value = Urls.EMPLOYEE_ADD_URL)
    public NetResult add(@RequestBody(required = false) Employee employee) {
        if (StringUtil.isEmpty(employee.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(employee.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        if (StringUtil.isEmpty(employee.getEmail())) {
            return ResultGenerator.genErrorResult(NetCode.EMAIL_INVALID, "邮箱不能为空");
        }
        if (StringUtil.isEmpty(employee.getPassword())) {
            employee.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        }
        Department department = iDepartmentService.find(employee.getDid());
        if (department == null) {
            return ResultGenerator.genErrorResult(NetCode.DEPARTMENT_ID_INVALID, "非法的部门id");
        }
        boolean result = iEmployeeService.add(employee);
        if (!result) {
            return ResultGenerator.genFailResult("添加员工失败");
        }
        return ResultGenerator.genSuccessResult(employee);
    }

    @PostMapping(value = Urls.EMPLOYEE_DELETE_URL)
    public NetResult delete(Long id) {
        try {
            iEmployeeService.remove(id);
            return ResultGenerator.genSuccessResult(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "删除员工失败！" + e.getMessage());
        }
    }

    @PostMapping(value = Urls.EMPLOYEE_UPDATE_URL)
    public NetResult update(@RequestBody Employee employee) {
        System.out.println(employee);
        try {
            if (StringUtil.isEmpty(employee.getPhone())) {
                return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
            }
            if (StringUtil.isEmpty(employee.getUsername())) {
                return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
            }
            if (StringUtil.isEmpty(employee.getEmail())) {
                return ResultGenerator.genErrorResult(NetCode.EMAIL_INVALID, "邮箱不能为空");
            }
            if (StringUtil.isEmpty(employee.getPassword())) {
                employee.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
            }
            Department department = iDepartmentService.find(employee.getDid());
            if (department == null) {
                return ResultGenerator.genErrorResult(NetCode.DEPARTMENT_ID_INVALID, "非法的部门id");
            }
            iEmployeeService.update(employee);
            return ResultGenerator.genSuccessResult(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR, "修改员工失败！" + e.getMessage());
        }
    }

    @GetMapping(value = Urls.EMPLOYEE_LIST_URL)
    public NetResult list() {
        List<Employee> employeeList = iEmployeeService.findEmployees();
        return ResultGenerator.genSuccessResult(employeeList);
    }

    @PostMapping(value = Urls.EMPLOYEE_LOGIN_URL)
    public NetResult login(@RequestBody Employee employee) {
        if (StringUtil.isEmpty(employee.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        if (StringUtil.isEmpty(employee.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        employee.setPassword(MD5Util.MD5Encode(employee.getPassword(), "utf-8"));
        Employee e=iEmployeeService.login(employee);
        if(e==null){
            return ResultGenerator.genFailResult("账号或密码错误");
        }
        else {
            String token= UUID.randomUUID().toString();
            logger.info("token__"+token);
            e.setToken(token);
            e.setPassword(null);
            redisTemplate.opsForValue().set(token,e,30, TimeUnit.MINUTES);
            return ResultGenerator.genSuccessResult(e);
        }

    }


}
