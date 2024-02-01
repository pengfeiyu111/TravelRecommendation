package cn.zwz.spot.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_evaluate")
@TableName("a_evaluate")
@ApiModel(value = "评价")
public class Evaluate extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "景点ID")
    private String spotId;

    @ApiModelProperty(value = "景点名称")
    private String spotName;

    @ApiModelProperty(value = "评价人ID")
    private String userId;

    @ApiModelProperty(value = "评价人")
    private String userName;

    @ApiModelProperty(value = "评价等级")
    private BigDecimal level;

    @ApiModelProperty(value = "评语")
    private String message;

    @ApiModelProperty(value = "评价时间")
    private String time;
}