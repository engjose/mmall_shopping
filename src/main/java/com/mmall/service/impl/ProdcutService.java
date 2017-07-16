package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProdcutService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductVo;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by panyuanyuan on 2017/6/25.
 */
@Service
public class ProdcutService implements IProdcutService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private IFileService fileService;

    @Override
    public ResultMap saveOrUpdate(Product product) {

        if(product == null) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }
        if(StringUtils.isNotBlank(product.getSubImages())) {
            String[] imagesArr = product.getSubImages().split(",");
            if(imagesArr.length >= 0) {
                product.setMainImage(imagesArr[0]);
            }
        }

        int rowCount = (null != product.getId()) ? productMapper.updateByPrimaryKey(product) : productMapper.insertSelective(product);
        if(rowCount > 0) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "操作成功");
        }
        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "操作失败");
    }

    @Override
    public ResultMap setProductStatus(Integer productId, Integer status) {

        if(null == productId || null == status) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }

        int rowCount = 0;
        Product product = productMapper.selectByPrimaryKey(productId);
        if(null != product) {
            product.setStatus(status);
            rowCount = productMapper.updateByPrimaryKeySelective(product);
        }

        if(rowCount > 0) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "更新成功");
        }
        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "更新失败");
    }

    @Override
    public ResultMap getProductDetail(Integer productId) {

        if(null == productId){
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品ID为空");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(null == product){
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品不 存在或者下架");
        }
        ProductDetailVo productDetailVo = assembleProductVoDetail(product);

        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "获取详情成功", productDetailVo);
    }

    @Override
    public ResultMap pageList(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList =  productMapper.selectList();
        List<ProductVo> productVoList = assembleProductListVo(productList);
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productVoList);

        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "获取成功", pageResult);
    }

    @Override
    public ResultMap pageSearch(String productName, Integer productId, Integer pageNo, Integer pageSize) {

        if(StringUtils.isBlank(productName) && null == productId) {
           return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Product> productList =  productMapper.selectByNameAndId(productName, productId);
        List<ProductVo> productVoList = assembleProductListVo(productList);
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productVoList);

        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "ok", pageResult);
    }

    @Override
    public ResultMap uploadFile(MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.uploadFile(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("url", url);
        fileMap.put("fileName", targetFileName);
        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "OK", fileMap);
    }

    /**
     * 封装productListVo对象
     *
     * @param productList
     * @return
     */
    private List<ProductVo> assembleProductListVo(List<Product> productList) {
        List<ProductVo> productVoList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(productList)) {
            for (Product product: productList) {
                ProductVo productVo = new ProductVo();
                BeanUtils.copyProperties(product, productVo);
                productVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://www.learning.image.com"));
                productVoList.add(productVo);
            }
        }

        return productVoList;
    }

    /**
     * 封装商品详情信息
     *
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductVoDetail(Product product) {

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);

        //设置其他属性
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.date2String(product.getCreateTime(), DateTimeUtil.STANDARD_FORMAT));
        productDetailVo.setUpdateTime(DateTimeUtil.date2String(product.getUpdateTime(), DateTimeUtil.STANDARD_FORMAT));

        return productDetailVo;
    }
}

















