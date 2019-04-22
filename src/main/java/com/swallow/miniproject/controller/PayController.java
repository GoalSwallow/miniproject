package com.swallow.miniproject.controller;

import com.swallow.miniproject.bean.Code;
import com.swallow.miniproject.bean.Retuen_Repay_Result;
import com.swallow.miniproject.bean.UnifiedOrder;
import com.swallow.miniproject.constant.Constant;
import com.swallow.miniproject.util.CheckUtil;
import com.swallow.miniproject.util.MessageUtil;
import com.swallow.miniproject.util.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import net.sf.json.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class PayController extends HttpServlet{


    public void center(HttpServletRequest request, HttpServletResponse response){

    }
    @PostMapping("pay")
    public void pay(JSONObject jsonObject){
        //将二维码url中的参数替换
        Constant.CODE_URL.replace("APPID", "微信公众号ID");
        try {
            WeChatUtil.doGetUrl(Constant.CODE_URL);
        } catch (IOException e) {
            System.out.println("模拟二维码扫码出错");
        }
    }
    @PostMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> map = null;
        try {
            map = MessageUtil.xmlToMap(request);//微信jdk 中一个XML转MAP的工具
            System.out.println(map);//将会显示{return_code=SUCCESS}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //生成商户订单，获取订单id
        UnifiedOrder unifiedOrder = new UnifiedOrder();
        //封装统一下单类信息
        unifiedOrder.setBody(map.get("body"));
        String xmlObject = MessageUtil.textMessageToXml(unifiedOrder);
        try {
            JSONObject jsonObject = WeChatUtil.doPoatUrl(Constant.UNIFIEDORDER_URL, xmlObject);
            if(jsonObject.getString("return_code ").equals("SUCCESS") && (jsonObject.getString("result_code")).equals("SUCCESS")){
                //return_code 和result_code都为SUCCESS
                String prepay_id = jsonObject.getString("prepay_id");
                String code_url = jsonObject.getString("code_url");
                Map<String, String> maps = new HashMap<>();
                maps.put("prepay_id", prepay_id);
                maps.put("code_url", code_url);
                Retuen_Repay_Result result = new Retuen_Repay_Result();
                //封装返回微信服务器，让用户完成支付信息result
                String xmlReturn = MessageUtil.textMessageToXml(request);
                PrintWriter writer = response.getWriter();
                //返回信息
                writer.print(xmlReturn);
            }
        } catch (IOException e) {
            System.out.println("调用统一下单接口出错！");
        }

        /**
         * 异步回调
         */
        @PostMapping("/notify")
        public ModelAndView notify(@RequestBody String notifyData) {

            payService.notify(notifyData);

            //返回给微信处理结果
            return new ModelAndView("pay/success");

        }
    }
}
