package com.example.testedutracker.Model;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String name;
    private String phone;
    private String search;
    private String state;
    private String grade;
    private String subject;
    private String student;
    private String parent;

    public User() {
    }

    //Users constructor
    public User(String id, String username, String imageURL, String name, String phone, String state) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.name = name;
        this.phone = phone;
        this.state = state;
    }

    //Teachers constructor
    public User(String id, String imageURL, String name, String phone, String search, String state, String subject, String username) {
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.phone = phone;
        this.search = search;
        this.state = state;
        this.subject = subject;
        this.username = username;
    }

    //Students constructor
    public User(String grade, String id, String imageURL, String parent, String phone, String search, String state, String student, String username) {
        this.grade = grade;
        this.id = id;
        this.imageURL = imageURL;
        this.parent = parent;
        this.phone = phone;
        this.search = search;
        this.state = state;
        this.student = student;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}