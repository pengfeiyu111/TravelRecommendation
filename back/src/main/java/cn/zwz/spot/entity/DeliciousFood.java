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
@Table(name = "a_delicious_food")
@TableName("a_delicious_food")
@ApiModel(value = "美食")
public class DeliciousFood extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "景点ID")
    private String spotId;

    @ApiModelProperty(value = "景点名称")
    private String spotName;

    @ApiModelProperty(value = "美食名称")
    private String title;

    @ApiModelProperty(value = "美食图片")
    private String image;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "简介")
    private String content;

    @ApiModelProperty(value = "人均消费")
    private BigDecimal avg;
}