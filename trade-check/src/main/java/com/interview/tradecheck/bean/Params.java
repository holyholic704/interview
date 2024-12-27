package com.interview.tradecheck.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiage
 */
@Data
@Accessors(chain = true)
public class Params {

    private String payment;

    private String filePath;

    private String merchant;

    private String format;

    private String dateFormat;

    private String split;

    private Integer date;

    private Integer code;

    private Integer amount;

    private String unit;

    private Integer fee;

    private Integer resource;

    private Integer systemCode;

    private String unzipCode;

    private Integer head;
}
