package org.imgoing.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @ApiModelProperty(value = "유저 id")
    private Long id;

    @ApiModelProperty(value = "유저 이름")
    private String name;

    @ApiModelProperty(value = "유저 닉네임")
    private String nickname;

    @ApiModelProperty(value = "유저 전화번호")
    private String phone;

    @ApiModelProperty(value = "유저 이메일")
    private String email;

    @ApiModelProperty(value = "유저 프로필")
    private String profile;
}
