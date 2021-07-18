package com.assertsl.workshop.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "camel-workshop.database")
public class DatabaseProperties {

    private String driver;
    private String url;
    private String username;
    private String password;

    private String getAllDrugs;
    private String getDrug;
    private String disableDrug;
    private String updateDrug;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getGetAllDrugs() {
        return getAllDrugs;
    }

    public void setGetAllDrugs(String getAllDrugs) {
        this.getAllDrugs = getAllDrugs;
    }

    public String getGetDrug() {
        return getDrug;
    }

    public void setGetDrug(String getDrug) {
        this.getDrug = getDrug;
    }

    public String getDisableDrug() {
        return disableDrug;
    }

    public void setDisableDrug(String disableDrug) {
        this.disableDrug = disableDrug;
    }

    public String getUpdateDrug() {
        return updateDrug;
    }

    public void setUpdateDrug(String updateDrug) {
        this.updateDrug = updateDrug;
    }
}
