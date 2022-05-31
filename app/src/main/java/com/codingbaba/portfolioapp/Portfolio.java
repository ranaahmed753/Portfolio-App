package com.codingbaba.portfolioapp;

public class Portfolio {
    private String name;
    private String image;
    private String phone;
    private String email;
    private String password;
    private String country;
    private String city;
    private String profession;

    public Portfolio() {
    }

    public Portfolio(String name, String image, String phone, String email, String password, String country, String city, String profession) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.profession = profession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

}
