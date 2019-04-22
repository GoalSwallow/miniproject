package com.swallow.miniproject.bean;

public class Return_UnifiedOrder extends Base {
    //返回状态码
    private String return_code;
    //返回信息
    private String return_msg;
    //业务结果
    private String result_code;
    //错误代码
    private String error_code;
    //错误代码描述
    private String err_code_des;
    //交易类型
    private String trade_type;
    //预支付交易会话标识
    private String prepay_id;
    //二维码链接
    private String code_url;
}
