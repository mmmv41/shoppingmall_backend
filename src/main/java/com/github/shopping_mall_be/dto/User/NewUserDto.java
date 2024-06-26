package com.github.shopping_mall_be.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class NewUserDto {   //유효성 검사 추가

    @NotBlank(message = "이메일을 입력해주세요.") //whitespace, null 불가
    @Email(message = "이메일 주소 형식에 맞게 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[A-Za-z]).{8,20}$",
            message = "비밀번호는 영문과 숫자만 가능하며, 8자리 이상이어야 합니다.")
    private final String user_password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String user_nickname;

    @NotBlank(message = "핸드폰번호를 입력해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})+$",
            message = "핸드폰 번호 형식에 맞게 입력해 주세요 ex)010-1234-5678")
    private String user_phone;

    @NotBlank(message = "주소를 입력해주세요.")
    private String user_addr;

    private MultipartFile user_img;

    public NewUserDto(String email, String userPassword, String userNickname, String userPhone, String userAddr, MultipartFile userImg) {
        this.email = email;
        this.user_password = userPassword;
        this.user_nickname = userNickname;
        this.user_phone = userPhone;
        this.user_addr = userAddr;
        this.user_img = userImg;
    }
}