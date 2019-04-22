package com.swallow.miniproject.bean;

import lombok.Data;

@Data
public class UnifiedOrder extends Base{

    //签名类型
    private String sign_type;
    //商品描述
    private String body;
    //商品详情
    private String detail;
    //附加数据
    private String attach;
    //商户订单号
    private String out_trade_no;
    //标价币种
    private String fee_type;
    //标价金额
    private int total_fee;
    //终端IP
    private String spbill_create_ip;
    //交易起始时间
    private String time_start;
    //交易结束时间
    private String time_expire;
    //订单优惠标记
    private String goods_tag;
    //通知地址
    private String notify_url;
    //交易类型
    private String trade_type;
    //商品ID
    private String product_id;
    //指定支付类型
    private String limit_pay;
    //用户标识
    private String openid;
    //电子发票入口开放标识
    private String receipt;
     //场景信息
    private String scene_info;
}
