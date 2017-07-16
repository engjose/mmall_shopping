package com.mmall.service;

import com.mmall.common.ResultMap;
import javax.servlet.http.HttpSession;

/**
 * Created by panyuanyuan on 2017/6/19.
 */
public interface ICategoryService {

    /**
     * 添加菜单选项
     *
     * @param name
     * @param parentId
     * @param session
     * @return
     */
    ResultMap addCategory(String name, Integer parentId, HttpSession session);

    /**
     * 根绝分类id更新分类
     *
     * @param id
     * @param name
     * @param parentId
     * @param session
     * @return
     */
    ResultMap updateCategory(Integer id, String name, Integer parentId, HttpSession session);

    /**
     * 获取某个id菜单下的子菜单
     *
     * @param parentId
     * @param session
     * @return
     */
    ResultMap getChildreParallelCategory(Integer parentId, HttpSession session);

    /**
     * 查询某一个菜单下的所有子菜单
     *
     * @param parentId
     * @param categoryService
     * @return
     */
    ResultMap getChildrenDeepCategory(Integer parentId, ICategoryService categoryService, HttpSession session);
}
