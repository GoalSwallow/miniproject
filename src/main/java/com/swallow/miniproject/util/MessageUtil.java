package com.swallow.miniproject.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.swallow.miniproject.bean.TextMessage;
import com.swallow.miniproject.constant.Constant;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

public class MessageUtil {

	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流     
		SAXReader reader = new SAXReader();
		// 生成document实体
		Document document = reader.read(inputStream);
		// 得到xml根元素       
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
			System.out.println(e.getName() + ":" + e.getText());
		}
		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

	/**
	 * 将类对象转化为XMl格式
	 * 
	 * @param message
	 * @return
	 */
	public static String textMessageToXml(Object message) {
		XStream xstream = new XStream();
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}

	/**
	 * 封装文本信息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName, String fromUserName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(Constant.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToXml(text);
	}

	/**
	 * 信息自动回复菜单列表
	 * 
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("感谢关注【广州租房小助手】无中介平台\n\n");
		sb.append("如果发布房源，发布求租【请留言给我们】\n\n");
		sb.append("记得留联系方式哦！文字不能超过800字！图片最好不要超过5张 \n");
		sb.append("(注:需要帮忙请后台回复 小编微信)");
		return sb.toString();
	}

	/**
	 * 当用户所发消息时包含租房发送
	 * 
	 * @return
	 */
	public static String listingMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href='http://2p38972u71.qicp.vip/listing.html'>发布房源</a>\n\n");
		sb.append("记得留联系方式哦！文字不能超过800字！图片最好不要超过5张\n\n");
		sb.append("请开始留言吧:");
		return sb.toString();
	}

	/**
	 * 当用户所发消息时包含求租发送
	 * 
	 * @return
	 */
	public static String rentMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href='http://2p38972u71.qicp.vip/rent.html'>发布求租</a>\n\n");
		sb.append("记得留联系方式哦！文字不能超过800字！图片最好不要超过5张\n\n");
		sb.append("请开始留言吧:");
		return sb.toString();
	}

	/**
	 * 当用户所发消息无法识别时发送
	 * 
	 * @return
	 */
	public static String notRecognized() {
		StringBuffer sb = new StringBuffer();
		sb.append("谢谢留言~~\r\n" + "\r\n" + "房源投稿----文字描述和图片请必须一次性发过来.标题必须自行写好 \r\n"
				+ "标题举例：【天河】X号线XX地铁站A口 XXXX(小区/村) 整屋出租 4200/月\r\n" + "\r\n" + "\r\n" + "请记得一定要留联系方式哦。文字不能超500字。\r\n"
				+ "\r\n" + " \r\n" + "每个工作日发布，请提前一天留言。当天留言默认隔天发布。\r\n" + "(注:需要帮忙请后台回复 小编微信)");
		return sb.toString();
	}
}
