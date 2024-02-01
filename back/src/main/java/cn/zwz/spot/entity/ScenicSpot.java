package cn.zwz.spot.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_scenic_spot")
@TableName("a_scenic_spot")
@ApiModel(value = "景点")
public class ScenicSpot extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 协同过滤算法：相似的用户可能喜欢相同物品
     * 收藏：10分
     * 打分数累加
     * 浏览：1分
     */
    @ApiModelProperty(value = "景点名称")
    private String title;

    @ApiModelProperty(value = "景点类型")
    private String type;
    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "评分")
    private BigDecimal star;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "收藏量")
    private Long collection;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "综合评分")
    private Long value;

    @ApiModelProperty(value = "门票价格")
    private BigDecimal price;

    @ApiModelProperty(value = "门票预订",notes = "提供购买链接，用户可以通过点击链接到其他平台购买门票")
    private String url;

    @ApiModelProperty(value = "开放时间")
    private String openTime;

    @ApiModelProperty(value = "景区地址")
    private String address;

    @ApiModelProperty(value = "景点介绍")
    private String content;

    @ApiModelProperty(value = "景点图片")
    private String image;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "是否收藏")
    private Boolean myCollection;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "我的评分")
    private BigDecimal myEvaluate;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "我的评分")
    private String myEvaluateMessage;
}