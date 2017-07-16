package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.Const.AlipayCallBack;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService orderService;

    /**
     * 支付宝支付订单
     *
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping("/alipay")
    public ResultMap pay(HttpSession session, Long orderNo, HttpServletRequest request) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        String path = request.getServletContext().getRealPath("upload");
        return orderService.pay(orderNo, loginUser.getId(), path);
    }

    /**
     * 支付宝回调接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/alipay/callback")
    public Object alipayCallBack(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> params = Maps.newHashMap();

        for(Iterator it = parameterMap.keySet().iterator(); it.hasNext();){
            String name = (String)it.next();
            String[] values = parameterMap.get(name);
            String valueStr = "";
            for(int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        //验证支付宝回调的正确性 非常重要
        params.remove("sign_type"); //根据支付宝的要求一处sign_type字段
        try {
            //验证是不是从支付宝来的请求
            boolean alipayRsa2Checked = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!alipayRsa2Checked) {
                return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "非法请求");
            }
        } catch (AlipayApiException e) {
            logger.error("验签失败");
        }

        //订单校验
        ResultMap resultMap = orderService.callBackAlipay(params);
        if(resultMap.getCode() == ResponseCode.SUCCESS.getCode()) {
            return AlipayCallBack.RESPONSE_SUCCESS;
        }
        return AlipayCallBack.RESPONSE_FAILED;
    }

    /**
     * 查询订单的支付状态
     *
     * @param orderNo
     */
    @RequestMapping("/payStatus")
    public ResultMap queryOrderPayStatus(Long orderNo, HttpSession session) {
        return orderService.queryOrderPayStatus(orderNo, session);
    }
}

