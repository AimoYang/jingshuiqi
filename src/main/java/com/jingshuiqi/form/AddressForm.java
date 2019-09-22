package com.jingshuiqi.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 18:26
 * @Description:
 */
@Data
public class AddressForm {

    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String addressArea;
    @NotNull
    private String addressDetail;
    @NotNull
    private Short isDefault;

}
