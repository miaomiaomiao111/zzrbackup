package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.system.agent.SystemApiAgent;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.code.ErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.common.enums.content.CourseChargeEnum;
import com.xuecheng.common.enums.content.CourseModeEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.common.util.StringUtil;

import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.common.util.QiniuUtils;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.convert.CoursePubConvert;
import com.xuecheng.content.convert.CourseTeacherConvert;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.content.entity.CourseMarket;
import com.xuecheng.content.entity.CoursePub;
import com.xuecheng.content.entity.CourseTeacher;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.qo.QueryCourseModel;
import com.xuecheng.content.service.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Autowired
    private CourseMarketService courseMarketService;

    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private SystemApiAgent systemApiAgent;

    @Autowired
    private CourseTeacherService courseTeacherService;

    @Autowired
    private CoursePubService coursePubService;

    @Override
    public List<CourseBase> queryAll() {
        List<CourseBase> result = list();
        return result;
    }

    /**
     * 1、分页查询 课程基本信息（CourseBase）集合数据
     * MP 有分页插件，并使用分页API来完成
     * 2、根据课程名称和审核状态条件进行数据查询
     * lambdaQueryWrapper
     * 3、教学机构职能查询到属于自己机构下的课程基本信息
     *
     * @param params 分页数据
     * @param model  查询条件数据
     * @return
     */
    @Override
    public PageVO queryCourseList(PageRequestParams params, QueryCourseModel model, Long companyId) {
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank((model.getAuditStatus()))) {
            // 添加的课程状态条件（eq）
            queryWrapper.eq(CourseBase::getAuditStatus, model.getAuditStatus());
        }

        if (StringUtil.isNotBlank(model.getCourseName())) {
            // 添加的课程名称条件（like）
            queryWrapper.like(CourseBase::getName, model.getCourseName());
        }
//        //机构分离
//        queryWrapper.eq(CourseBase::getCompanyId, companyId);

        // 适配运营平台和教学机构条件
        queryWrapper.eq(!(ObjectUtils.isEmpty(companyId)), CourseBase::getCompanyId, companyId);

        //创建分页数据
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());
        //根据分页和查询调价查询list数据
        Page<CourseBase> pageResult = this.page(page, queryWrapper);

        long total = pageResult.getTotal();
        List<CourseBase> records = pageResult.getRecords();

        // 代码
        List<CourseBaseDTO> dtos = Collections.EMPTY_LIST;
        // 使用mapstruct转换器将pos转为dtos
        if (!(CollectionUtils.isEmpty(records))) {
            dtos = CourseBaseConvert.INSTANCE.entitys2dtos(records);
        }

        PageVO pageVO = new PageVO(dtos, total, params.getPageNo(), params.getPageSize());

        return pageVO;
    }


    @Override
    //1.开启事务
    @Transactional
    public CourseBaseDTO createCourseBase(CourseBaseDTO courseBaseDTO) {

        //2.数据库表的约束:
        //2.1companyId,name,mt,st,grade,teachmode,auditstatus
        //2.2前端页面中的非空（红色*）: users,charge

        //2.3如果课程是收费：价格必须存在
        if (CourseChargeEnum.CHARGE_YES.getCode().equals(courseBaseDTO.getCharge())) {

            if (ObjectUtils.isEmpty(courseBaseDTO.getPrice())) {
                throw new RuntimeException("收费课程价格不能为空");
            }

        }
        //   3.将dto数据转为po数据
        CourseBase courseBase = CourseBaseConvert.INSTANCE.dto2entity(courseBaseDTO);
        //   4.对新增的课程添加审核的默认状态：未提交
        courseBase.setAuditStatus(CourseAuditEnum.AUDIT_UNPAST_STATUS.getCode());
        //   5.保存数据并判断是否保存成功
        boolean baseFlag = this.save(courseBase);
        if (!baseFlag) {
            throw new RuntimeException("保存课程基础信息失败");
        }
        //   6.保存课程营销数据并判断是否成功
        CourseMarket courseMarket = new CourseMarket();

        courseMarket.setCourseId(courseBase.getId());
        courseMarket.setCharge(courseBaseDTO.getCharge());
        courseMarket.setPrice(courseBaseDTO.getPrice());

        boolean marketFlag = courseMarketService.save(courseMarket);
        if (!marketFlag) {
            throw new RuntimeException("保存课程营销数据失败");
        }
        //   7.将保存成功的数据转为dto数据并返回
        CourseBaseDTO resultDto = CourseBaseConvert.INSTANCE.entity2dto(courseBase);
        resultDto.setCharge(courseMarket.getCharge());
        resultDto.setPrice(courseMarket.getPrice());


        return resultDto;
    }

    @Override
    public CourseBaseDTO getCourseBase(Long courseBaseId, Long companyId) {
        // 1.判断关键数据
        // courseBaseId  companyId
        if (ObjectUtils.isEmpty(courseBaseId) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            throw new RuntimeException("关键数据不能为空");
        }
        //2.查询数据
        CourseBase courseBase = getCourseBaseByIdAndCompanyId(courseBaseId, companyId);
        //  3.判断业务数据
        //    课程基本信息
        //    判断课程信息是否存在,判断是否是同一家机构， 判断课程是否删除
        if (ObjectUtils.isEmpty(courseBase)) {
            throw new RuntimeException("课程信息不存在");
        }


        if (CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus())) {
            throw new RuntimeException("课程信息已被删除");
        }


        CourseBaseDTO courseBaseDTO = CourseBaseConvert.INSTANCE.entity2dto(courseBase);
        //查询营销信息表
        LambdaQueryWrapper<CourseMarket> marketQueryWrapper = new LambdaQueryWrapper<>();
        marketQueryWrapper.eq(CourseMarket::getCourseId, courseBaseId);
        CourseMarket courseMarket = courseMarketService.getOne(marketQueryWrapper);
        //设置营销信息
        courseBaseDTO.setCharge(courseMarket.getCharge());
        courseBaseDTO.setPrice(courseMarket.getPrice());

        return courseBaseDTO;

    }

    @Override
    @Transactional
    public CourseBaseDTO modifyCourseBase(CourseBaseDTO dto) {
        // 判断关键数据
        verifyCourseMsg(dto);
        //判断课程Id是否存在
        Long courseBaseId = dto.getCourseBaseId();
        if (ObjectUtils.isEmpty(courseBaseId)) {
            throw new RuntimeException("课程id不能为空");
        }
        //判断课程信息是否存在
        Long companyId = dto.getCompanyId();
        CourseBase courseBase = getCourseBaseByIdAndCompanyId(courseBaseId, companyId);

        if (ObjectUtils.isEmpty(courseBase)) {
            throw new RuntimeException("课程信息不存在");
        }
        //判断课程信息是否已删除
        if (CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus())) {
            throw new RuntimeException("课程信息已被删除");
        }
        //判断课程状态是否异常
        String auditStatus = courseBase.getAuditStatus();

        if (CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus)
        ) {
            throw new RuntimeException("课程审核状态异常");
        }
        //修改基础课程信息
        CourseBase courseBasePo = CourseBaseConvert.INSTANCE.dto2entity(dto);

        boolean courseBaseFlag = this.updateById(courseBasePo);

        if (!courseBaseFlag) {
            throw new RuntimeException("修改课程信息失败");
        }
        //修改营销信息
        LambdaUpdateWrapper<CourseMarket> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.set(CourseMarket::getCharge, dto.getCharge());
        updateWrapper.set(CourseMarket::getPrice, dto.getPrice());
        updateWrapper.eq(CourseMarket::getCourseId, courseBaseId);

        boolean marketResult = courseMarketService.update(updateWrapper);

        if (!marketResult) {
            throw new RuntimeException("修改课程营销数据失败");
        }


        //将结果内容返回
        CourseBaseDTO resultDTO = CourseBaseConvert.INSTANCE.entity2dto(courseBasePo);

        resultDTO.setCharge(dto.getCharge());
        resultDTO.setPrice(dto.getPrice());


        return resultDTO;
    }

    @Override
    public void removeCourseBase(Long courseBaseId, Long companyId) {

        //1.判断关键数据的合法性
        if (ObjectUtils.isEmpty(courseBaseId) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            throw new RuntimeException("课程id不能为空");
        }


        //根据Id获得课程基础信息并进行相应的判断
        // 获得课程基础信息
        CourseBase courseBase = getById(courseBaseId);
        if (ObjectUtils.isEmpty(courseBase)) {
            throw new RuntimeException("课程信息不存在");
        }
        //判断课程信息是否已删除
        if (CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus())) {
            throw new RuntimeException("课程信息已被删除");
        }
        // 判断课程的审核状态
        String auditStatus = courseBase.getAuditStatus();

        if (CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)
        ) {
            throw new RuntimeException("课程审核状态异常");
        }

        // 修改课程信息状态值
        LambdaUpdateWrapper<CourseBase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CourseBase::getStatus, CommonEnum.DELETE_FLAG.getCodeInt());
        updateWrapper.set(CourseBase::getChangeDate, LocalDateTime.now());

        updateWrapper.eq(CourseBase::getId, courseBase.getId());

        boolean result = update(updateWrapper);
        if (!result) {
            throw new RuntimeException("删除失败");
        }


    }

    /**
     * 业务分析：
     * 1.判断关键数据
     * courseBaseId  companyId
     * 2.判断业务数据
     * 课程基础信息
     * 判断课程是否存在
     * 判断是否是同一家机构
     * 判断课程是否被删除
     * 判断课程的审核状态
     * 3.提交课程
     * 修改课程基础信息的审核状态：已提交
     * 4.判断修改后的结果内容
     */
    @Override
    public void commitCourseBase(Long courseBaseId, Long companyId) {
        //1.判断关键数据
        //courseBaseId  companyId
        if (ObjectUtils.isEmpty(companyId) || ObjectUtils.isEmpty(courseBaseId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        //判断课程是否存在 and 是否是同一家机构
        CourseBase courseBase = getCourseBaseByIdAndCompanyId(courseBaseId, companyId);
        if (ObjectUtils.isEmpty(courseBase)) {
            ExceptionCast.cast(ContentErrorCode.E_120013);
        }
        // 判断课程是否被删除
        ExceptionCast.cast(CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus())
                , ContentErrorCode.E_120018);

        // 判断课程的审核状态
        String auditStatus = courseBase.getAuditStatus();

        if (CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus)
        ) {
            ExceptionCast.cast(ContentErrorCode.E_120015);
        }
        // 3. 提交课程
        // 修改课程基础信息的审核状态：已提交
        LambdaUpdateWrapper<CourseBase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CourseBase::getAuditStatus, CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode());
        updateWrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
        updateWrapper.eq(CourseBase::getId, courseBaseId);
        // 4. 判断修改后的结果内容
        boolean updateFlag = this.update(updateWrapper);
        ExceptionCast.cast(!updateFlag, ContentErrorCode.E_120017);

    }

    /*
     * 业务分析：
     *   1.判断关键数据
     *       courseId           课程id
     *       courseAuditStatus  课程审核状态
     *       courseAutditMind   课程审核意见
     *   2.判断业务数据
     *       课程基础信息
     *           判断课程是否存在
     *           判断课程是否删除
     *           判断课程审核状态：只能是已提交
     *       运营平台的审核状态
     *           只能是 审核通过或审核未通过
     *   3.修改课程审核的数据
     *       修改CourseBase表
     *           audtiStatus
     *           auditMind
     *           auditNum
     *           changeDate
     *   4.判断修改的结果
     * */
    @Override
    public void approve(CourseBaseDTO dto) {
        // 1.判断关键数据
        // courseId           课程id
        // courseAuditStatus  课程审核状态
        // courseAutditMind   课程审核意见
        String auditStatus = dto.getAuditStatus();
        if (ObjectUtils.isEmpty(dto.getCourseBaseId()) ||
                StringUtil.isBlank(auditStatus) ||
                StringUtil.isBlank(dto.getAuditMind())
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //  2.判断业务数据
        CourseBase courseBase = this.getById(dto.getCourseBaseId());
        // 判断课程是否存在
        ExceptionCast.cast(ObjectUtils.isEmpty(courseBase),
                ContentErrorCode.E_120013);
        // 判断课程是否删除
        ExceptionCast.cast(CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus()),
                ContentErrorCode.E_120018);

        // 判断课程审核状态：只能是已提交
        ExceptionCast.cast(!(CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(courseBase.getAuditStatus())),
                ContentErrorCode.E_120015);

        // 2.2  判断运营平台的审核状态
        //      运营平台：只能是 审核通过或审核未通过
        if (CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_UNPAST_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100108);
        }
        // 3.修改课程审核的数据
        //     修改CourseBase表
        //         audtiStatus状态
        //         auditMind审核意见
        //         auditNum审核次数
        //         changeDate审核时间
        LambdaUpdateWrapper<CourseBase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CourseBase::getAuditStatus, auditStatus);
        updateWrapper.set(CourseBase::getAuditMind, dto.getAuditMind());
        updateWrapper.set(CourseBase::getAuditNums, courseBase.getAuditNums() + 1);
        updateWrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
        updateWrapper.eq(CourseBase::getId, courseBase.getId());

        // 4.判断修改的结果
        boolean result = this.update(updateWrapper);
        ExceptionCast.cast(!result, ContentErrorCode.E_120017);

    }


    private CourseBase getCourseBaseByIdAndCompanyId(Long courseBaseId, Long companyId) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(CourseBase::getId, courseBaseId);
        queryWrapper.eq(!ObjectUtils.isEmpty(companyId),CourseBase::getCompanyId, companyId);

        CourseBase courseBase = this.getOne(queryWrapper);
        return courseBase;
    }

    /*
     * 校验课程的关键数据
     *   数据库表的约束: companyId,name,mt,st,grade,teachmode,auditstatus
         前端页面中的非空（红色*）: users,charge
         如果课程是收费：价格必须存在
         修改的数据必有Id值
     * */
    private void verifyCourseMsg(CourseBaseDTO courseBaseDTO) {
        if (ObjectUtils.isEmpty(courseBaseDTO.getCompanyId())) {

            throw new RuntimeException("公司id不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getName())) {
            throw new RuntimeException("课程名称不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getMt())) {
            throw new RuntimeException("课程大分类不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getSt())) {
            throw new RuntimeException("课程小分类不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getGrade())) {
            throw new RuntimeException("课程等级不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getTeachmode())) {
            throw new RuntimeException("课程教学模式不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getUsers())) {
            throw new RuntimeException("使用人群不能为空");
        }

        if (StringUtil.isBlank(courseBaseDTO.getCharge())) {
            throw new RuntimeException("课程收费不能为空");
        }
    }

    @Override
    public Map<String, Object> preview(Long courseBaseId, Long companyId) {
        // 1.获得CoursePub数据
        CoursePub coursePub = generateCoursePub(courseBaseId, companyId, false);
        // 2.根据CoursePub来生成DataMap
        Map<String, Object> dataMap = generateDataMap(coursePub);

        return dataMap;
    }

    @Value("${course.publish.topic}")
    private String topicName;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /*
     * 课程发布
     */

    @Override
    @Transactional
    public void publish(Long courseBaseId, Long companyId) {
        // 1.判断关键数据
        //
        if (ObjectUtils.isEmpty(courseBaseId) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //  2.判断业务数据
        //      课程的基本信息
        //          判断是否存在
        //          判断是否是同一家机构
        //          判断是否删除
        //          判断审核状态：审核通过
        verifyCourseCoreMsg(courseBaseId, companyId, true);
        //  3.发送事务消息
        //    topicname（nacos）、message（courseBaseid）、null
        Message<Long> message = MessageBuilder.withPayload(courseBaseId).build();
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(topicName, message, null);
        //获取本地事务状态
        LocalTransactionState localTransactionState = result.getLocalTransactionState();
        if (!(LocalTransactionState.COMMIT_MESSAGE == localTransactionState)) {
            log.error("发送消息发送发的事务消息失败：{}", localTransactionState);
        }

    }

    //执行本地事务保存CoursePub并更改课程审核状态
    @Override
    @Transactional
    public void excuteProducerLocalData(Long courseBaseId) {
        //1.根据CourseBaseId值来判断业务数据
        //     课程的基本信息
        //         判断是否存在
        //         判断是否删除
        //         判断审核状态：审核通过
        generateCoursePub(courseBaseId, null, true);

    }
    @Autowired
    private Configuration configuration;

    @Value("${file.qiniu.accessKey}")
    private String accessKey;

    @Value("${file.qiniu.secretKey}")
    private String secretKey;

    @Value("${file.qiniu.bucket}")
    private String bucket;

    @Value("${course.publish.position}")
    private String position;

    /*
     * 业务分析：
     *   1.判断关键的业务数据
     *       课程基础信息（审核状态：课程已发布）
     *       课程发布数据是否存在
     *   2.生成课程的详情页面（页面的静态化）
     *   3.将课程详情页发布到指定的位置（七牛云服务--CDN）
     * */
    @Override
    public void publishPage(Long courseBaseId) {


        //  课程发布数据是否存在
        LambdaQueryWrapper<CoursePub> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoursePub::getCourseId, courseBaseId);

        CoursePub coursePub = coursePubService.getOne(queryWrapper);

        if (CoursePub.IS_PUB.equals(coursePub.getIsPub())) {
            log.error("课程已经发布");
            return;
        }

        String htmlString = "";
        try {
            // 2.生成课程的详情页面（页面的静态化）
            /*
             * 生成课程详情页：
             *   1.数据模型DataMap
             *       数据模型源于：CoursePub数据
             *   2.页面模板--Freemarker的Configuration对象来获得
             * */
            // 2.1 获得数据模型
            Map<String, Object> dataMap = generateDataMap(coursePub);

            // 2.2 获得页面模板
            Template template = configuration.getTemplate("learing_article.ftl");

            htmlString = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap);
        } catch (Exception e) {
            log.error("课程发布生成课程详情页失败：{}", e.getMessage());
        }

        // 3.将课程详情页发布到指定的位置（七牛云服务--CDN）


        try {
        /*
        String accessKey, String secretKey, String bucket, String contentText, String fileKey
        * 参数：
        *   1.accessKey
            2.secretKey
            3.bucket
            以上的七牛云配置在nacos中
            4.contentText 页面静态化后的String
            5.fileKey 文件key
                文件key一定是：课程发布id+.html
                        29.html
        *
        * */
            // pages/29.html
            String fileKey = position + coursePub.getId() + ".html";
            QiniuUtils.upload2Qiniu(accessKey, secretKey, bucket, htmlString, fileKey);

        } catch (Exception e) {
            log.error("上传七牛云服务课程详情页失败：{}", e.getMessage());
            throw e;
        }
        //3.修改课程发布状态
        LambdaUpdateWrapper<CoursePub> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CoursePub::getIsPub, CoursePub.IS_PUB);
        updateWrapper.eq(CoursePub::getId, coursePub.getId());
        boolean update = coursePubService.update(updateWrapper);
        if (!update) {
            ExceptionCast.cast(ContentErrorCode.E_120204);
        }

    }


    /* 生成数据模型DataMap */
    private Map<String, Object> generateDataMap(CoursePub coursePub) {
        HashMap<String, Object> dataMap = new HashMap<>();
        //1.课程发布key
        //
        //     ${coursePub.xxxx}
        dataMap.put("coursePub", coursePub);
        // 2.课程营销key
        //
        //     ${courseMarket.xxxx}
        String market = coursePub.getMarket();
        CourseMarket courseMarket = JsonUtil.jsonToObject(market, CourseMarket.class);
        dataMap.put("courseMarket", courseMarket);
        // 3.课程模式key值
        //
        //     ${courseTeachModeEnums}
        //
        // 参考项目枚举类 CourseModeEnum 中的数据。
        // 任意一个枚举类中都会有一个方法：values()
        //    values()：获得该枚举类中的所有多例数据
        dataMap.put("courseTeachModeEnums", CourseModeEnum.values());
        // 4.课程计划key值
        //
        //     ${teachplanNode}
        //
        // 上面的四个 key 为数据模型中制定的主键值。
        String teachplan = coursePub.getTeachplan();
        TeachplanDTO teachplanDTO = JsonUtil.jsonToObject(teachplan, TeachplanDTO.class);
        dataMap.put("teachplanNode", teachplanDTO);
        return dataMap;
    }

    private CoursePub generateCoursePub(Long courseBaseId, Long companyId, Boolean isPublish) {
        // 1.判断关键数据
        if (isPublish) {
            ExceptionCast.cast(ObjectUtils.isEmpty(courseBaseId), CommonErrorCode.E_100101);
        } else {
            if (ObjectUtils.isEmpty(courseBaseId) ||
                    ObjectUtils.isEmpty(companyId)
            ) {
                ExceptionCast.cast(CommonErrorCode.E_100101);
            }
        }
        //2.判断业务数据
        //  课程基本信息
        //  判断是否是同一家机构
        //  判断是否删除
        //  判断审核状态：未提交、审核未通过。
        CourseBase courseBase = verifyCourseCoreMsg(courseBaseId, companyId, isPublish);
        //  3.获得课程预览的数据
        //      课程基本信息
        //      课程营销
        //      课程计划（树形机构）
        //      课程教师信息
        //      完善课程分类数据
        //          转为json：课程营销、课程计划、课程教师信息
        //      PS：冗余字段（change、price）
        //      默认数据：is_pub、status（数据库有默认数据赋值，无需操作）
        // 3.1 获得课程营销
        LambdaQueryWrapper<CourseMarket> marketQueryWrapper = new LambdaQueryWrapper<>();
        marketQueryWrapper.eq(CourseMarket::getCourseId, courseBaseId);
        //判断营销数据
        CourseMarket courseMarket = courseMarketService.getOne(marketQueryWrapper);
        ExceptionCast.cast(org.springframework.util.ObjectUtils.isEmpty(courseMarket), ContentErrorCode.E_120101);
        // 3.2获得课程计划（树形机构）
        TeachplanDTO nodes = teachplanService.queryTreeNodesByCourseId(courseBaseId, courseBase.getCompanyId());


//        // 3.3 获得课程教师信息--学员实现
//        // TODO
//        List<CourseTeacherDTO> teacherDTOList = courseTeacherService.queryTeachersByCourseBaseId(courseBaseId, companyId);
//        List<CourseTeacher> courseTeachers = CourseTeacherConvert.INSTANCE.dto2entitys(teacherDTOList);
        // 3.4 完善课程分类数据  mk大分类
        String mt = courseBase.getMt();
        RestResponse<CourseCategoryDTO> mtResponse = systemApiAgent.getById(mt);

        if (!(mtResponse.isSuccessful())) {
            ExceptionCast.castWithCodeAndDesc(mtResponse.getCode(), mtResponse.getMsg());
        }
        CourseCategoryDTO mtResponseResult = mtResponse.getResult();
        //小分类
        String st = courseBase.getSt();
        RestResponse<CourseCategoryDTO> stResponse = systemApiAgent.getById(st);

        if (!(stResponse.isSuccessful())) {
            ExceptionCast.castWithCodeAndDesc(stResponse.getCode(), stResponse.getMsg());
        }

        CourseCategoryDTO stResponseResult = stResponse.getResult();
        // 3.5 转为json：课程营销、课程计划、课程教师信息
        String marketJsonString = JsonUtil.objectTojson(courseMarket);
        String nodesJsonString = JsonUtil.objectTojson(nodes);
        // 教师信息-学员实现
//        // TODO()
//        String teachers = JsonUtil.objectTojson(courseTeachers);
//        String teacherJsonString = JsonUtil.objectTojson(teachers);

        // 3.6 冗余字段（change、price）
        String charge = courseMarket.getCharge();
        Float price = courseMarket.getPrice();
        //  4.创建CoursePub数据，保存内容到数据库中
        //      判断课程发布数据是否存在（根据数据：courseBaseId）
        //          如果有：修改
        //          如果没有：新增
        // 课程基础信息
        CoursePub coursePub = CoursePubConvert.INSTANCE.courseBase2coursePub(courseBase);
        // 课程营销
        coursePub.setMarket(marketJsonString);
        // 课程计划
        coursePub.setTeachplan(nodesJsonString);
//        // TODO: 教师json赋值
//        coursePub.setTeachers(teacherJsonString);
        // 课程分类数据
        coursePub.setMtName(mtResponseResult.getName());
        coursePub.setStName(stResponseResult.getName());

        // 冗余字段
        coursePub.setPrice(price);
        coursePub.setCharge(charge);
        // 判断课程发布数据是否存在（根据数据：courseBaseId）
        // 一个课程的基本信息对应一个课程发布数据
        LambdaQueryWrapper<CoursePub> pubQueryWrapper = new LambdaQueryWrapper<>();
        pubQueryWrapper.eq(CoursePub::getCourseId, courseBaseId);

        CoursePub coursePubPo = coursePubService.getOne(pubQueryWrapper);

        if (ObjectUtils.isEmpty(coursePubPo)) {
            // 添加课程发布数据

            // 将coursePub和courseBase数据进行关联操作
            coursePub.setCourseId(courseBaseId);

            coursePub.setId(null);

            boolean result = coursePubService.save(coursePub);

            ExceptionCast.cast(!result, ContentErrorCode.E_120205);

        } else {
            // 修改课程发布数据

            coursePub.setId(coursePubPo.getId());

            boolean result = coursePubService.updateById(coursePub);

            ExceptionCast.cast(!result, ContentErrorCode.E_120205);
        }
        // 如果是课程发布，修改课程基本信息中的审核状态
        if (isPublish) {

            LambdaUpdateWrapper<CourseBase> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(CourseBase::getAuditStatus, CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode());
            updateWrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
            updateWrapper.eq(CourseBase::getId, courseBase.getId());

            boolean result = this.update(updateWrapper);

            ExceptionCast.cast(!(result), ContentErrorCode.E_120205);
        }

        //  5.返回结果数据 CoursePub
        return coursePub;
    }

    private CourseBase verifyCourseCoreMsg(Long courseBaseId, Long companyId, Boolean isPulish) {

        CourseBase courseBase = getCourseBaseByIdAndCompanyId(courseBaseId, companyId);

        if (org.springframework.util.ObjectUtils.isEmpty(courseBase)) {
            ExceptionCast.cast(ContentErrorCode.E_120013);
        }

        if (CommonEnum.DELETE_FLAG.getCodeInt().equals(courseBase.getStatus())) {
            ExceptionCast.cast(ContentErrorCode.E_120018);
        }

        String auditStatus = courseBase.getAuditStatus();

        if (isPulish) {

            // 如果是课程发布的判断课程审核状态：只需要判断--课程审核通过
            ExceptionCast.cast(!(CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus)),
                    ContentErrorCode.E_120015);

        } else {
            if (CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus) ||
                    CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus) ||
                    CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus)
            ) {
                ExceptionCast.cast(ContentErrorCode.E_120015);
            }
        }

        return courseBase;
    }


}

