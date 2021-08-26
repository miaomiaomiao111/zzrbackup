
package com.xuecheng.learning.common.constant;


import com.xuecheng.common.domain.code.ErrorCode;

/**
 * 学习中心服务业务异常枚举类
 * <hr>
 * 后四位错误代码在开发者开发中进行定义<br>
 * 学习中心服务前两位错误服务标识为：20(在 {@link ErrorCode} 中定义)
 * 模块错误代码定义如下：
 * <ul>
 *   <li>课程学习--定义模块代码为： 01</li>
 * </ul>
 *
 * @Description: 定义学习中心服务中的业务异常代码
 */
public enum LearningErrorCode implements ErrorCode {
	E_200101(200001,"获得视频播放地址失败"),

	E_202000(202000,"接口调用创建账号异常"),
	E_202001(202001,"接口调用检查用户名异常"),
	E_202002(202002,"接口调用检查手机号异常"),


	E_202100(202100,"该用户账号已存在"),
	E_202101(202101,"该手机号已注册过"),

	E_202102(202102,"验证手机号失败"),
	E_202103(202103,"该账号被禁用"),

	E_202104(202104,"验证流程异常，请重新找回密码"),
	E_202105(202105,"你输入的新密码和确认密码不一致"),

	E_202106(202106,"该成员已存在于本机构下"),



	E_202200(202200,"此直播未开始或已结束"),

	E_202201(202201,"请您先支付过后再继续学习"),
	E_202202(202202,"学习的课程不存在"),
	E_202203(202203,"课程的订单数据不存在"),
	E_202204(202204,"操作用户课程学习记录数据失败"),
	E_202205(202205,"学习的课程数据不存在");


	private int code;
	private String desc;

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	LearningErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	public static LearningErrorCode setErrorCode(int code) {
		for (LearningErrorCode errorCode : LearningErrorCode.values()) {
			if (errorCode.getCode()==code) {
				return errorCode;
			}
		}
		return null;
	}
}
