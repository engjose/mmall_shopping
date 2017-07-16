package com.mmall.controller.backend;

import com.mmall.common.ResultMap;
import com.mmall.service.ICategoryService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by panyuanyuan on 2017/6/19.
 */
@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManagerController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 添加菜单
     *
     * @param name
     * @param parentId
     * @param session
     * @return
     */
    @RequestMapping(value = "/add")
    public ResultMap addCategory(String name, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId, HttpSession session) {
        return categoryService.addCategory(name, parentId, session);
    }

    /**
     * 更新菜单
     *
     * @param name
     * @param id
     * @param parentId
     * @param session
     * @return
     */
    @RequestMapping(value = "/update")
    public ResultMap updateCategory(String name, Integer id, @RequestParam(required = false, defaultValue = "0") Integer parentId, HttpSession session) {
        return categoryService.updateCategory(id, name, parentId, session);
    }

    /**
     * 查询某个菜单下的所有平级菜单
     *
     * @param parentId
     * @param session
     * @return
     */
    @RequestMapping(value = "/getParallelCategory")
    public ResultMap getChildrenParallelCategory(@RequestParam(required = true, defaultValue = "0") Integer parentId, HttpSession session) {
        return categoryService.getChildreParallelCategory(parentId, session);
    }

    /**
     * 查询某个菜单下的所有递归子菜单
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getDeepCategory/{parentId}")
    public ResultMap getChildrenDeepCategory(@PathVariable Integer parentId, HttpSession session) {
        return categoryService.getChildrenDeepCategory(parentId, categoryService, session);
    }
}
