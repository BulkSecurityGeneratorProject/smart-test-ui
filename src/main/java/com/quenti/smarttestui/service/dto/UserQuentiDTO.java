package com.quenti.smarttestui.service.dto;

/**
 * Created by juarez on 13/03/17.
 */


//crear el dto con los datos q necesite
//    crear el usuario
//    user service
//    reset key y reset date null
//    devuelve un dto con el username y password
//    comunicar con el login, $broadcast, $on
//    onlogin
//    dto normal, este mae hace el login por q el usuario ya existe


public class UserQuentiDTO {

    private String username;
    private String password;
    private String useripAddress;
    private String userClientId;
    private String objTokenDTO;
    private String firstName;
    private String lastName;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getObjTokenDTO() {
        return objTokenDTO;
    }

    public void setObjTokenDTO(String objTokenDTO) {
        this.objTokenDTO = objTokenDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUseripAddress() {
        return useripAddress;
    }

    public void setUseripAddress(String useripAddress) {
        this.useripAddress = useripAddress;
    }

    public String getUserClientId() {
        return userClientId;
    }

    public void setUserClientId(String userClientId) {
        this.userClientId = userClientId;
    }

    @Override
    public String toString() {
        return "UserQuentiDTO{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", useripAddress='" + useripAddress + '\'' +
            ", userClientId='" + userClientId + '\'' +
            '}';
    }


}

