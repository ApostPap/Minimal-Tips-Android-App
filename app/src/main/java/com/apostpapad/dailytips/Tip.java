package com.apostpapad.dailytips;

import com.google.gson.annotations.SerializedName;

public class Tip {

    @SerializedName("tip_string")
    private String tipString;

    @SerializedName("response")
    private String response;

    public Tip(String  tip_string)
    {
        tipString = tip_string;
    }


    public String getTipString() {
        return tipString;
    }

    public String getResponse() {
        return response;
    }
}
