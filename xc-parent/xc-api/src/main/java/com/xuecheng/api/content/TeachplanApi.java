package com.xuecheng.api.content;


import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.api.content.model.vo.BindTeachplanMediaVO;
import com.xuecheng.api.content.model.vo.TeachplanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value = "Teachplan controller", tags = "课程计划信息管理")
public interface TeachplanApi {

    @ApiOperation(value ="根据课程id查询计划树形结构三级目录")
    @ApiImplicitParam(name ="courseId", value ="课程Id", required = true, paramType ="path", dataType ="Long")
    TeachplanDTO queryTreeNodeByCourseId(@PathVariable("courseId") Long courseId);

    @ApiOperation(value= "新增或修改课程计划")
    @ApiImplicitParam(name = "teachplanVO",value = "课程计划VO" ,
            required = true, dataType = "TeachplanVO",paramType = "body")
    TeachplanDTO createOrModifyTeachPlan(TeachplanVO teachplanVO);

    @ApiOperation("课程计划绑定媒资信息")
    TeachplanMediaDTO associateMedia(BindTeachplanMediaVO vo);
}
