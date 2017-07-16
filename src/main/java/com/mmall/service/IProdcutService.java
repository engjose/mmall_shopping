package com.mmall.service;

import com.mmall.common.ResultMap;
import com.mmall.pojo.Product;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by panyuanyuan on 2017/6/25.
 */
public interface IProdcutService {

    /**
     * 更新或者保存商品
     *
     * @param product
     * @return
     */
    ResultMap saveOrUpdate(Product product);

    /**
     * 设置商品的状态
     *
     * @param productId
     * @param status
     * @return
     */
    ResultMap setProductStatus(Integer productId, Integer status);

    /**
     * 获取商品的详情信息
     *
     * @param productId
     * @return
     */
    ResultMap getProductDetail(Integer productId);

    /**
     * 分页查询商品信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultMap pageList(Integer pageNum, Integer pageSize);

    /**
     * 根据商品名称模糊查询和ID查询
     *
     * @param productName
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    ResultMap pageSearch(String productName, Integer productId, Integer pageNo, Integer pageSize);

    /**
     * 上传文件到FTP服务器
     *
     * @param file
     * @param request
     * @return
     */
    ResultMap uploadFile(MultipartFile file, HttpServletRequest request);
}
