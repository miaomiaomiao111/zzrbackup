package com.xuecheng.system.common.constant;

import com.xuecheng.common.domain.code.ErrorCode;

/**
 * <p>
 *     异常信息类的编写：
 *      1.实现 ErrorCode接口
 *      2.声明两个属性
 *          code-错误代码
 *          desc-错误信息
 *      3.提供构造方法
 *          枚举中的每个实例中传入参数的个数，要匹配构造方法
 *      4.枚举异常信息
 *          异常信息变量名：E_错误代码(错误代码，错误信息);
 *              错误代码: 6位
 *                          xx   （12）              xx                xx   （00或01开始）
 *                    业务中的错误代码       业务模块中的错误代码     具体信息的错误代码（有程序员自行定义）
 *
 *              错误信息: 程序员通过文字来描述错误代码
 *
 *      5.完善接口实现方法中的返回参数
 * 模块错误代码定义如下：
 *  <ul>
 *    <li>数据字典--定义模块代码为： 00</li>   1200xx01
 *    <li>课程分类--定义模块代码为： 01</li>
 *  </ul>
 *
 *   PS：枚举类时JavaBean对象的多例形式
 *      一个类的对象在内存存在多个实例
 *      枚举实现java中的常量：多值常量
 *      单值常量：
 *          1.java类 class 中实现
 *          2.java类 interface 中实现（推荐--省略掉 public static final ）
 * </p>
 *
 * @Description:
 */
public enum SystemErrorCode implements ErrorCode {

    //课程分类
    E_110100(110100,"课程分类数据不存在"),
    ;

    // 声明两个属性
    private int code;
    private String desc;

    // 提供构造方法
    SystemErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
