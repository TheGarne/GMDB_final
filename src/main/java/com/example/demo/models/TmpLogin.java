package com.example.demo.models;

import com.example.demo.Repository.DataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TmpLogin {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String tmpUsername;
    private String tmpPassword;

    public TmpLogin(String tmpUsername, String tmpPassword) {
        this.tmpUsername = tmpUsername;
        this.tmpPassword = tmpPassword;
    }

    public TmpLogin() {
    }

    public String getTmpUsername() {
        return tmpUsername;
    }

    public void setTmpUsername(String tmpUsername) {
        this.tmpUsername = tmpUsername;
    }

    public String getTmpPassword() {
        return tmpPassword;
    }

    public void setTmpPassword(String tmpPassword) {
        this.tmpPassword = tmpPassword;
    }
}
