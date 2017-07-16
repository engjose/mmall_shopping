package com.mmall.controller.backend;

import com.mmall.common.ResultMap;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProdcutService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by panyuanyuan on 2017/6/25.
 */
@RestController
@RequestMapping(value = "/manage/product")
public class ProductManagerController {

    @Autowired
    private IProdcutService prodcutService;

    @Autowired
    private IFileService fileService;

    /**
     * 保存或者更新商品
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResultMap saveOrUpdate(@RequestBody Product product) {
        return prodcutService.saveOrUpdate(product);
    }

    /**
     * 设置商品的销售状态
     *
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    public ResultMap setProductStatus(Integer productId, Integer status) {
        return prodcutService.setProductStatus(productId, status);
    }

    /**
     * 获取商品的详情
     *
     * @return
     */
    @RequestMapping(value = "/getDetail/{productId}")
    public ResultMap getProductDetail(@PathVariable("productId") Integer productId) {
        return prodcutService.getProductDetail(productId);
    }

    /**
     * 分页查询商品信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    public ResultMap pageList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return prodcutService.pageList(pageNum, pageSize);
    }

    /**
     * 根据商品名称模糊查询&ID锁定查询
     *
     * @param productName
     * @param productId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/pageSearch", method = RequestMethod.GET)
    public ResultMap pageSearch(String productName, Integer productId, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return prodcutService.pageSearch(productName, productId, pageNo, pageSize);
    }


    /**
     * 上传文件到FTP服务器
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResultMap uploadFile(MultipartFile file, HttpServletRequest request) {
        return prodcutService.uploadFile(file, request);
    }

}
