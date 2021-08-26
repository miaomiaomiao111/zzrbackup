package com.xuecheng.search.common.constant;


import com.xuecheng.common.domain.code.ErrorCode;
import com.xuecheng.common.exception.ExceptionCast;

/**
 * 课程搜索服务业务异常枚举类
 * <hr>
 * 后四位错误代码由开发者开发中进行定义<br>
 * 课程搜索服务前两位错误服务标识为：15(在 {@link ErrorCode} 中定义)
 * 模块错误代码定义如下：
 * <ul>
 *   <li>xxxx--定义模块代码为： 00</li>
 * </ul>
 */
public enum ContentSearchErrorCode implements ErrorCode {
	/**
	 * 不存在的用户信息
	 */
	E_150001(150001,"请求失败"),
	E_150002(150002,"查询索引数据失败"),

	E_150003(15003, "课程不存在");

	private int code;
	private String desc;
		
	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	ContentSearchErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	public static ContentSearchErrorCode setErrorCode(int code) {
       for (ContentSearchErrorCode errorCode : ContentSearchErrorCode.values()) {
           if (errorCode.getCode()==code) {
               return errorCode;
           }
       }
       return null;
	}
}
