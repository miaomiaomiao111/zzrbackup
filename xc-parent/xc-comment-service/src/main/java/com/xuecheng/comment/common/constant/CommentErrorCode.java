
package com.xuecheng.comment.common.constant;


import com.xuecheng.common.domain.code.ErrorCode;

/**
 * 评论服务业务异常枚举类
 * <hr>
 * 后四位错误代码在开发者开发中进行定义<br>
 * 评论管理服务前两位错误服务标识为：18(在 {@link ErrorCode} 中定义)
 * 模块错误代码定义如下：
 * <ul>
 *   <li>xxxx--定义模块代码为： 00</li>
 * </ul>
 *
 * @Description: 定义评论服务中的业务异常代码
 * @author : Flippy
 * @date: 2019/9/11
 */
public enum CommentErrorCode implements ErrorCode {
	E_180001(180001,"保存失败"),
	E_180002(180002,"评论不存在,或评论不属于当前机构"),
	E_180003(180003,"删除失败"),

	E_180100(180100,"该评论禁止回复"),
	;

	private int code;
	private String desc;
		
	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	CommentErrorCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	public static CommentErrorCode setErrorCode(int code) {
       for (CommentErrorCode errorCode : CommentErrorCode.values()) {
           if (errorCode.getCode()==code) {
               return errorCode;
           }
       }
	       return null;
	}
}
