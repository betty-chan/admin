package cn.exrick.xboot.modules.base.entity;

import cn.exrick.xboot.base.XbootBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author chenxin
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_announce")
@TableName("t_announce")
public class Announce extends XbootBaseEntity {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "显示位置")
    private String showPage;

    @ApiModelProperty(value = "显示时长")
    private Integer showTime;

    @ApiModelProperty(value = "状态")
    private String status;
}
