package com.cq.musicplayer.JavaBean;

import java.io.Serializable;

public class PhoneUserBean implements Serializable {
    private String phone;
    private String country;

    public PhoneUserBean(String phone, String country) {
        this.phone = phone;
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
