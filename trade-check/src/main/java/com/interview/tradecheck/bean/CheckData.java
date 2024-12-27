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

    /**
     * 1：正常
     * 2：多收
     * 3：少收
     * 4：金额多收
     * 5：金额少收
     */
    private Integer status;
}
