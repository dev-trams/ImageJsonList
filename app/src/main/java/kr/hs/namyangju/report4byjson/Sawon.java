package kr.hs.namyangju.report4byjson;

public class Sawon {
    String id;
    String name;
    String gender;
    String salary;
    String imgUrl;
    public Sawon() {}
    public Sawon(String id, String name, String gender, String salary, String imgUrl) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
