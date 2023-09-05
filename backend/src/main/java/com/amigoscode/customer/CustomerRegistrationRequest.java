package com.amigoscode.customer;

import java.util.Objects;

public record CustomerRegistrationRequest(String name, String email, Integer age, String gender) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CustomerRegistrationRequest) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.age, that.age) &&
                Objects.equals(this.gender, that.gender);
    }

    @Override
    public String toString() {
        return "CustomerRegistrationRequest[" +
                "name=" + name + ", " +
                "email=" + email + ", " +
                "age=" + age + ", " +
                "gender=" + gender + ']';
    }

}
