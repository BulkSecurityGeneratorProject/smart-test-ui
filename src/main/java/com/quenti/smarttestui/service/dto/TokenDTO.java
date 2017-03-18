package com.quenti.smarttestui.service.dto;

/**
 * Created by juarez on 15/03/17.
 */
public class TokenDTO {

    private String sessionKey;
    private String expirationDate;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
            "sessionKey='" + sessionKey + '\'' +
            ", expirationDate='" + expirationDate + '\'' +
            '}';
    }
}
