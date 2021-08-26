
package com.xuecheng.order.common.constant;


import com.xuecheng.common.domain.code.ErrorCode;

/**
 * 订单管理服务业务异常枚举类
 * <hr>
 * 后四位错误代码由开发者开发中进行定义<br>
 * 订单管理服务前两位错误服务标识为：16(在 {@link ErrorCode} 中定义)
 * 模块错误代码定义如下：
 * <ul>
 *   <li>xxxx--定义模块代码为： 00</li>
 * </ul>
 *
 * @Description: 定义订单管理中的业务异常代码
 */
public enum OrderErrorCode implements ErrorCode {
	E_160001(160001,"保存失败"),
	E_160002(160002,"获取课程信息失败"),
	E_160003(160003,"该课程为免费学习课程"),

	E_160004(160004,"已支付的课程订单不能删除"),
	E_160005(160005,"未支付的课程订单只能在过期后删除"),
	E_160006(160006, "只有未支付的课程订单才能付款"),

	E_160007(160007, "购买课程信息不存在"),
	E_160008(160008, "订单信息不存在"),
	E_160009(160009, "创建支付订单失败"),
	E_160010(160010, "支付通知获得失败消息"),

	E_160011(160011, "发送订单支付事务消息失败"),
	E_160012(160012, "修改订单支付状态结果失败"),
	E_160013(160013, "修改订单状态结果失败"),
	E_160014(160014, "订单状态异常"),
    E_160015(160015, "修改订单数据失败"),
    E_160016(160016, "获得支付连接失败"),
    E_160017(160017, "金额数据异常"),
    E_160018(160018, "订单支付数据不存在"),
    E_160019(160019, "解析支付结果通知失败"),
    E_160020(160020, "修改订单支付数据失败"),

	;

	private int code;
	private String desc;
		
	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	OrderErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	public static OrderErrorCode setErrorCode(int code) {
       for (OrderErrorCode errorCode : OrderErrorCode.values()) {
           if (errorCode.getCode()==code) {
               return errorCode;
           }
       }
	       return null;
	}
}
