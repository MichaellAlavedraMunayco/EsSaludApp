package com.aplicacion.essalud.models;

public class Message {

    public Message(String message, String issuerName) {
        this.message = message;
        this.issuerName = issuerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    private String message;
    private String issuerName;
    public static String USER = "User";
    public static String CHATBOT = "ChatBot";
}
