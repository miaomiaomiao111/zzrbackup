package com.xuecheng.api.order.model.pay;

import com.xuecheng.common.enums.common.CommonEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @Description:
 */
@Data
@ApiModel("获得支付链接地址封装类")
public class PayCodeUrlResult {

    public static final String WX_PAY_SUCCESS_FLAG = "SUCCESS";
    public static final String WX_PAY_FLAG = "WX";
    public static final String AlI_PAY_FLAG = "ALI";

    @ApiModelProperty("生成二维码的状态：1-成功，0-失败")
    private String status;
    @ApiModelProperty("二维码链接地址")
    private String codeURL;
    @ApiModelProperty("异常信息：获得链接地址失败时，message才会有值")
    private String message;


    /*
    * 成功获得支付url数据
    * */
    public static PayCodeUrlResult success(String codeURL) {
        PayCodeUrlResult result = new PayCodeUrlResult();
        result.setStatus(CommonEnum.USING_FLAG.getCode());
        result.setCodeURL(codeURL);
        return result;
    }


    /*
     * 失败获得支付url数据
     * */
    public static PayCodeUrlResult failed(String message) {
        PayCodeUrlResult result = new PayCodeUrlResult();
        result.setStatus(CommonEnum.DELETE_FLAG.getCode());
        result.setMessage(message);
        return result;
    }

}
