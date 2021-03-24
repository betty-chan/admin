package cn.exrick.xboot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class SearchVo implements Serializable {

    private String startDate;

    private String endDate;
}
