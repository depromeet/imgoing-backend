package org.imgoing.api.dto.auth;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoTokenVerifiedResponse {
    private String id;
    private String connected_at;
    @SerializedName("kakao_account")
    private KakaoAccountData kakaoAccountData;

    @Getter
    public class KakaoAccountData {
        @SerializedName("profile_needs_agreement")
        private boolean profileNeedsAgreement;

        @SerializedName("has_email")
        private boolean has_email;

        @SerializedName("email_needs_agreement")
        private boolean emailNeedsAgreement;

        @SerializedName("is_email_valid")
        private boolean isEmailValid;

        @SerializedName("is_email_verified")
        private boolean isEmailVerified;

        private String email;
        private Profile profile;

        @Getter
        public class Profile {
            private String nickname;
            @SerializedName("thumbnail_image_url")
            private String thumbnailImage;

            @SerializedName("profile_image_url")
            private String profileImage;
        }
    }
}
