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
@Table(name = "a_collection")
@TableName("a_collection")
@ApiModel(value = "收藏")
public class Collection extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "景点ID")
    private String spotId;

    @ApiModelProperty(value = "景点名称")
    private String spotName;

    @ApiModelProperty(value = "照片")
    private String image;

    @ApiModelProperty(value = "收藏人ID")
    private String userId;

    @ApiModelProperty(value = "收藏人")
    private String userName;

    @ApiModelProperty(value = "收藏时间")
    private String time;
}