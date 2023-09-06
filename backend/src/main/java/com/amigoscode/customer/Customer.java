package com.amigoscode.customer;

import com.amigoscode.exception.RequestValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
    name="customer",
    uniqueConstraints = {
            @UniqueConstraint(
                name="customer_email_unique",
                    columnNames="email"
            )
    }
)


public class Customer {

    public enum Gender {
        MALE("Male"), FEMALE("Female"), OTHER("Other");
        private final String value;

        Gender(String value) {
            this.value = value;
        }

        @JsonCreator
        public static Gender fromValue(String value) {
            for (Gender gender : Gender.values()) {
                if (gender.value.equalsIgnoreCase(value)) {
                    return gender;
                }
            }
            throw new RequestValidationException("Please choose between 'Male', 'Female' or 'Other'. Gender: '" + value + "' is invalid");
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Gender{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    @Id
    @SequenceGenerator(
            name="customer_id_seq",
            sequenceName="customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Integer age;


    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Customer() {
    }

    public Customer(String name, String email, Integer age, String gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = Gender.fromValue(gender);
    }

    public Customer(Long id, String name, String email, Integer age, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = Gender.fromValue(gender);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender.getValue();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return age == customer.age && Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(gender.getValue(), customer.gender.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, gender);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
