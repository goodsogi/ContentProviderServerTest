package com.commax.contentproviderservertest.sqlite;

/**
 * 디바이스 모델
 * Created by bagjeong-gyu on 2016. 8. 19..
 */
public class Device {
    private String deviceType;
    private String ipAddress;
    private String sipPhoneNo;




    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSipPhoneNo() {
        return sipPhoneNo;
    }

    public void setSipPhoneNo(String sipPhoneNo) {
        this.sipPhoneNo = sipPhoneNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
