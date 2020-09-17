package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName StrategicParametersDTO
 * @Description
 * @Author hanxf
 * @Date 2019/5/17 14:29
 * @Version 1.0
 **/
@Data
@ToString
public class StrategicParametersDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private String mapKey;

    private Long sales;

    private Long unitPrice;

    private Long costPlus;
}
