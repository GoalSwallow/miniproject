package com.swallow.miniproject.bean;

import lombok.Data;

@Data
public class Retuen_Repay_Result extends Base{
    //返回状态码
    private String return_code;
    //返回信息
    private String return_msg;
    //预支付ID
    private String prepay_id;
    //业务结果
    private String result_code;
    //错误描述
    private String err_code_des;
}
