package com.momodo.userApp.domain;

import com.momodo.commons.BaseTimeEntity;
import com.momodo.userApp.dto.RequestUpdateUserProfile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.momodo.aws.S3UploadService.DEFAULT_IMAGE_NAME;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_app")
public class UserApp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_app_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;  //계정구분

    @Enumerated(EnumType.STRING)
    private Role roles;

    @Column(name = "password")
    private String password;  //비밀번호

    @Column(name = "name")
    private String name;  //이름

    @Column(name="email")
    private String email;  //이메일

    @Column(name = "phone")
    private String phone;  //전화번호

    @Column(name = "tier")
    private String tier;

    @Column(name = "is_push")
    private Boolean isPush = Boolean.TRUE;  //알림동의

    @Column(name = "is_marketing")
    private Boolean isMarketing = Boolean.FALSE;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;  //사용여부

    @Column(name = "access_token")
    private String accessToken;  //로그인 토큰

    @Column(name="refresh_token")
    private String refreshToken;  //리프레쉬 토큰

    @Column(name = "token_weight")
    private Long tokenWeight;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "introduce")
    private String introduce;

    @Builder
    public UserApp(String userId, UserType type, String password, String name, String email, boolean isPush, boolean isActive, String accessToken, String refreshToken, String phone) {
        this.userId = userId;
        this.type = type;
        this.password = password;
        this.name = name;
        this.tier = Tier.RED.name();
        this.roles = Role.ROLE_MEMBER;
        this.phone = phone;
        this.email = email;
        this.isPush = isPush;
        this.isActive = isActive;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenWeight = 1L;
        this.profileImage = DEFAULT_IMAGE_NAME;
        this.introduce = "";
    }

    public void setTodoTier(Tier todoTier){
        this.tier = todoTier.name();
    }

    public void updateProfile(String profileImage, RequestUpdateUserProfile updateUserProfile){
        this.profileImage = profileImage;
        this.name = updateUserProfile.getName();
        this.introduce = updateUserProfile.getIntroduce();
    }

    public void userAppWithdrawal() {
        isActive = false;
    }

    public void increaseTokenWeight() {
        this.tokenWeight++;
    }

}
