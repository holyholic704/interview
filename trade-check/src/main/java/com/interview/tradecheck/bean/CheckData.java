package com.interview.tradecheck.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiage
 */
@Data
@Accessors(chain = true)
public class CheckData {

    private String payment;

    private String merchant;

    private String dayTime;

    private String code;

    private String systemCode;

    private Long amount;

    private Long fee;

    private String resource;

    private Boolean status;
}
