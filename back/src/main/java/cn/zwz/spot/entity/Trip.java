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

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_trip")
@TableName("a_trip")
@ApiModel(value = "行程")
public class Trip extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "景点ID")
    private String spotId;

    @ApiModelProperty(value = "景点名称")
    private String spotName;

    @ApiModelProperty(value = "行程名称")
    private String title;

    @ApiModelProperty(value = "起点")
    private String start;

    @ApiModelProperty(value = "终点")
    private String end;

    @ApiModelProperty(value = "交通方式")
    private String way;

    @ApiModelProperty(value = "行程路线")
    private String content;

    @ApiModelProperty(value = "图片")
    private String imageUrl;
}