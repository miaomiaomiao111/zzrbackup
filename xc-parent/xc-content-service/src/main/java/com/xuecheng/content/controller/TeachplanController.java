package com.xuecheng.content.controller;

import com.xuecheng.api.content.TeachplanApi;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.api.content.model.vo.BindTeachplanMediaVO;
import com.xuecheng.api.content.model.vo.TeachplanVO;
import com.xuecheng.content.common.util.UAASecurityUtil;
import com.xuecheng.content.convert.TeachplanConvert;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程计划 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class TeachplanController implements TeachplanApi {

    @Autowired
    private TeachplanService  teachplanService;

    @GetMapping("teachplan/{courseId}/tree-nodes")
    public TeachplanDTO queryTreeNodeByCourseId(@PathVariable("courseId") Long courseId) {

        Long companyId = UAASecurityUtil.getCompanyId();

       TeachplanDTO dto = teachplanService.queryTreeNodesByCourseId(courseId,companyId);
       return  dto;

    }

    @PostMapping("teachplan")
    public TeachplanDTO createOrModifyTeachPlan(@RequestBody TeachplanVO teachplanVO) {

        //1.获得公司Id
        Long companyId = UAASecurityUtil.getCompanyId();

        //2.将vo数据转为 dto 数据
        TeachplanDTO dto = TeachplanConvert.INSTANCE.vo2dto(teachplanVO);

        //3.调用service获得 dto数据
        TeachplanDTO result = teachplanService.createOrModifyTeachPlan(dto, companyId);
        return result;

    }

    @PostMapping("teachplan/media/association")
    public TeachplanMediaDTO associateMedia(@RequestBody BindTeachplanMediaVO vo) {

        Long companyId = UAASecurityUtil.getCompanyId();

        TeachplanMediaDTO teachplanMediaDTO = new TeachplanMediaDTO();

        teachplanMediaDTO.setTeachplanId(vo.getTeachplanId());
        teachplanMediaDTO.setMediaId(vo.getMediaId());


        TeachplanMediaDTO resultDTO = teachplanService.associateMedia(teachplanMediaDTO, companyId);

        return resultDTO;
    }
}
