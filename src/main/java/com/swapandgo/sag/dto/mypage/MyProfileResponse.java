package com.swapandgo.sag.dto.mypage;

import com.swapandgo.sag.domain.user.User;
import lombok.Getter;

@Getter
public class MyProfileResponse {
    private final Long userId;
    private final String email;
    private final String username;
    private final MyAddressResponse address;

    public MyProfileResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        if (user.getAddress() == null) {
            this.address = new MyAddressResponse(null, null, null);
        } else {
            this.address = new MyAddressResponse(
                    user.getAddress().getCountry(),
                    user.getAddress().getRegion(),
                    user.getAddress().getStreet()
            );
        }
    }
}
