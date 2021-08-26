package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseBaseApi;
import com.xuecheng.api.content.model.vo.CourseBaseVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.content.common.util.UAASecurityUtil;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.model.qo.QueryCourseModel;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class CourseBaseController implements CourseBaseApi {

    @Autowired
    private CourseBaseService courseBaseService;

    @PostMapping("/course/list")
    public PageVO queryCourseList(PageRequestParams params,
                                  @RequestBody QueryCourseModel model) {

        //1.获得访问令牌并从中解析出机构的信息Id数据
        long companyId = UAASecurityUtil.getCompanyId();

        //2.调用service层的方法
        PageVO<CourseBaseDTO> pageVo = courseBaseService.queryCourseList(params, model, companyId);

        return pageVo;
    }


    @PostMapping("course")
    public CourseBaseDTO createCourseBase(@RequestBody CourseBaseVO courseBaseVO) {

        CourseBaseDTO courseBaseDTO = CourseBaseConvert.INSTANCE.vo2dto(courseBaseVO);

        //机构id
        Long companyId = UAASecurityUtil.getCompanyId();
        //设置机构id
        courseBaseDTO.setCompanyId(companyId);
        return courseBaseService.createCourseBase(courseBaseDTO);
    }

    @GetMapping(value = "course/{courseBaseId}")
    public CourseBaseDTO getCourseBase(@PathVariable("courseBaseId") Long courseBaseId) {

        //1.获得公司Id值
        Long companyId = UAASecurityUtil.getCompanyId();

        //2.获得课程基本信息数据
        CourseBaseDTO courseBase = courseBaseService.getCourseBase(courseBaseId, companyId);

        return courseBase;
    }

    @PutMapping("course")
    public CourseBaseDTO modifyCourseBase(@RequestBody CourseBaseVO courseBaseVO) {

        //1.将VO数据转为DTO数据
        CourseBaseDTO courseBaseDTO = CourseBaseConvert.INSTANCE.vo2dto(courseBaseVO);


        //2.通过工具类获得公司和用户信息
        Long companyId = UAASecurityUtil.getCompanyId();
        courseBaseDTO.setCompanyId(companyId);

        return courseBaseService.modifyCourseBase(courseBaseDTO);
    }

    @DeleteMapping("course/{courseBaseId}")
    public void removeCoursebase(@PathVariable Long courseBaseId) {
        Long companyId = UAASecurityUtil.getCompanyId();
        courseBaseService.removeCourseBase(courseBaseId, companyId);
    }

    @GetMapping("course/commit/{courseBaseId}")
    public void commitCourseBase(@PathVariable("courseBaseId") Long courseBaseId) {
        Long companyId = UAASecurityUtil.getCompanyId();

        courseBaseService.commitCourseBase(courseBaseId, companyId);
    }

    @GetMapping("course/preview/{courseBaseId}/{companyId}")
    public Object preview(@PathVariable Long courseBaseId, @PathVariable Long companyId) {

        // 此controller：返回的结果要包含两个内容；
        // 1.数据模型  2. 页面模板的名称

        Map<String, Object> dataMap = courseBaseService.preview(courseBaseId, companyId);

        ModelAndView modelAndView = new ModelAndView("learing_article", dataMap);

        return modelAndView;
    }

    @PostMapping("course_pub/publish/{courseId}")
    public void publish(@PathVariable Long courseId) {

        Long companyId = UAASecurityUtil.getCompanyId();

        courseBaseService.publish(courseId,companyId);

    }
}
