package org.nb.petHome.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.nb.petHome.common.Urls;
import org.nb.petHome.entity.Department;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.net.param.DepartmentParam;
import org.nb.petHome.service.IDepartmentService;
import org.nb.petHome.utils.ResultGenerator;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@Api(tags = "部门接口文档")
@RestController
public class DepartmentController {

    private IDepartmentService iDepartmentService;

    public DepartmentController(IDepartmentService iDepartmentService){
        this.iDepartmentService = iDepartmentService;
    }


    @ApiOperation("添加部门")
    @PostMapping(value = Urls.DEPARTMENT_ADD_URL)
    public NetResult add(@RequestBody  DepartmentParam  departmentParam){
        System.out.println("添加"+departmentParam);
        try {
            Department department = new Department();
            department.setSn(departmentParam.getSn());
            department.setName(departmentParam.getName());
            long parent_id = departmentParam.getParentId();
            Department parentDepartment = new Department();
            parentDepartment.setId(parent_id);
            department.setParent(parentDepartment);
            iDepartmentService.add(department);
            return ResultGenerator.genSuccessResult(department);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.CREATE_DEPARTMENT_ERROR, "创建对象失败");
        }
    }

    @PostMapping(value = Urls.DEPARTMENT_DELETE_URL)
    public NetResult delete(Long id){
        try {
            iDepartmentService.remove(id);
            return ResultGenerator.genSuccessResult(id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR,"删除部门失败！"+e.getMessage());
        }
    }

    @PostMapping(value = Urls.DEPARTMENT_UPDATE_URL)
    public NetResult update(@RequestBody Department department){
        try {
            iDepartmentService.update(department);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR,"更新部门失败！"+e.getMessage());
        }
    }

    @GetMapping(value = Urls.DEPARTMENT_GET_URL)
    public NetResult get(Long id){
        Department department = iDepartmentService.find(id);
        return ResultGenerator.genSuccessResult(department);
    }

    @GetMapping(value = Urls.DEPARTMENT_LIST_URL)
    public NetResult list(){
        List<Department> department = iDepartmentService.findAll();
        return ResultGenerator.genSuccessResult(department);
    }

    @GetMapping(value = Urls.DEPARTMENT_TREE_URL)
    public NetResult tree(){
        List<Department> department = iDepartmentService.getDepartmentTreeData();
        return ResultGenerator.genSuccessResult(department);
    }
}
