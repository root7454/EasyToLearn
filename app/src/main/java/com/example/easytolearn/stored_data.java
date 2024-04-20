package com.example.easytolearn;

public class stored_data {
    String name, email, password;
    int usertype;



    public int getUsertype(){
        return usertype;
    }

    public void setUsertype(int usertype){
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public stored_data( String name, String email, String password, int usertype) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
    }

    public stored_data() {
    }
}
