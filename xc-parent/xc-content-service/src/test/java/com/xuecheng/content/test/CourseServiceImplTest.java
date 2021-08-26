//package com.xuecheng.content.test;
//
//import com.xuecheng.common.domain.page.PageRequestParams;
//import com.xuecheng.common.domain.page.PageVO;
//import com.xuecheng.common.enums.content.CourseAuditEnum;
//import com.xuecheng.api.content.model.dto.CourseBaseDTO;
//import com.xuecheng.content.model.qo.QueryCourseModel;
//import com.xuecheng.content.service.CourseBaseService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class CourseServiceImplTest {
//    @Autowired
//    private CourseBaseService courseBaseService;
//
//    @Test
//    public void testQueryCourseList() {
//
//        //1.分页数据
//        PageRequestParams params = new PageRequestParams();
//        params.setPageSize(10);
//
//        //2.查询条件数据
//        QueryCourseModel model = new QueryCourseModel();
//
//        model.setCourseName("Spring");
//
//        model.setAuditStatus(CourseAuditEnum.AUDIT_PASTED_STATUS.getCode());
//        PageVO<CourseBaseDTO> result = courseBaseService.queryCourseList(params, model,Long);
//
//        System.out.println("result =>" + result.getItems());
//
//        System.out.println("total =>" + result.getCounts());
//
//    }
//}