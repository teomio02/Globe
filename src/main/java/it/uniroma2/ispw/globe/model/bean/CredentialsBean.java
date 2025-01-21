package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class CredentialsBean {
    private String username;
    private String password;
    private String type;
    private String description;
    private List<String> preferences;

    public CredentialsBean(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public CredentialsBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password;}

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description; }

    public List<String> getPreferences() { return preferences; }

    public void setPreferences(List<String> preferences) { this.preferences = preferences; }
}
