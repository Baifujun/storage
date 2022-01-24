package com.baixs.storage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author baifujun
 * @date 2021-09-17
 * @time 17:45
 */
@Data
public class TestVO {
    @ApiModelProperty(required = true, value = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date date;
}
