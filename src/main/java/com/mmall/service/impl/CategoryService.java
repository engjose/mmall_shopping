package com.mmall.service.impl;

import com.google.common.collect.Sets;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by panyuanyuan on 2017/6/19.
 */
@Service
public class CategoryService implements ICategoryService {

    private static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResultMap addCategory(String name, Integer parentId, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(null == loginUser) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户没有登录");
        }

        if(Const.ROL_MANAGER  != loginUser.getRole()) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "不是管理员");
        }

        if(parentId == null || StringUtils.isBlank(name)) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }

        Category category = new Category();
        category.setName(name);
        category.setStatus(true);
        category.setParentId(parentId);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());

        int rowCount = categoryMapper.insertSelective(category);
        if(rowCount > 0) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "保存成功");
        }

        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "保存失败");
    }

    @Override
    public ResultMap updateCategory(Integer id, String name, Integer parentId, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(null == loginUser) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if(Const.ROL_MANAGER != loginUser.getRole()) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "不是管理员");
        }

        if(null == id || StringUtils.isBlank(name)) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setUpdateTime(new Date());
        if(null != parentId) {
            category.setParentId(parentId);
        }

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "更新成功");
        }
        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "更新失败");
    }

    @Override
    public ResultMap getChildreParallelCategory(Integer parentId, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(null == loginUser) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        if(Const.ROL_MANAGER != loginUser.getRole()) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "不是管理员");
        }

        List<Category> list = categoryMapper.getChildreParallelCategory(parentId);
        if(CollectionUtils.isEmpty(list)) {
            logger.error("没有查出来菜单");
        }
        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "获取成功", list);
    }

    /**
     * 递归查询本节点的id和孩子节点的ID
     *
     * @param parentId
     * @param categoryService
     * @return
     */
    @Override
    public ResultMap getChildrenDeepCategory(Integer parentId, ICategoryService categoryService, HttpSession session) {

        Set<Category> categorySet = Sets.newHashSet();
        findCategories(categorySet, parentId);

        //去除父节点
        Category category = new Category();
        category.setId(parentId);
        categorySet.remove(category);

        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "获取成功", categorySet);
    }

    /**
     * 递归算法查找子节点
     *
     * @param categorySet
     * @param parentId
     * @return
     */
    private Set<Category> findCategories(Set<Category> categorySet, Integer parentId) {
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if(null != category) {
            categorySet.add(category);
        }

        //查找子节点
        List<Category> categoryList = categoryMapper.getChildreParallelCategory(parentId);
        if(!CollectionUtils.isEmpty(categoryList)) {
            for (Category categoryItems : categoryList) {
                findCategories(categorySet, categoryItems.getId());
            }
        }
        return categorySet;
    }
}
