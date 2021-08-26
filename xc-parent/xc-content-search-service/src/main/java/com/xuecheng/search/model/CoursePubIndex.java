package com.xuecheng.search.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程发布
 */
@Data
public class CoursePubIndex implements Serializable {

    private Long id;

    private Long course_id;

    private Long company_id;

    private String company_name;

    private String name;

    private String users;

    private String tags;

    private String mt;

    private String st;

    private String grade;

    private String teachmode;

    private String pic;

    private String description;

    private String teachplan;

    private Date create_date;

    private Date change_date;

    private Integer is_latest;

    private Integer is_pub;

    private String status;

    private String remark;

    private String charge;

    private Float price;

    private String valid;

    /**
     * 学习人数
     */
    private Long learners;

    /**
     * 评论数
     */
    private Long comment_num;

}
