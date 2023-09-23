package com.aiit.lj;

import java.io.Serializable;
import java.util.Date;

public class Reader extends User implements Serializable {
	
    private int id;
    private String username;
    private String gender;
    private Date birthday;
    private Date registTime;
    
  
    public Reader(int id, String loginName, String username, String gender, Date birthday, Date registTime) {
        super(loginName, generateDefaultPassword());
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.birthday = birthday;
        this.registTime = registTime;
    }
    

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Date getRegistTime() {
        return registTime;
    }

    private static String generateDefaultPassword() {
        return "default123";
    }
}
