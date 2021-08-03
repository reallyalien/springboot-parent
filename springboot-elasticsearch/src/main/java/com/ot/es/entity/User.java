package com.ot.es.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@ToString
public class User {
    private String name;
    private Integer age;
    private String sex;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(age, user.age) &&
                Objects.equals(sex, user.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, sex);
    }

    public static void main(String[] args) {
//        Optional<Integer> optional = Optional.of(1);
        Optional<Object> empty = Optional.empty();
        empty.orElseThrow(() -> new RuntimeException(""));
//        Optional<Integer> integer = Optional.ofNullable(1);
        User user = null;
        Optional.ofNullable(user).orElse(createUser());
        Optional.ofNullable(user).orElseGet(User::createUser);
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException());
        Optional<User> user2 = Optional.ofNullable(user).filter(user1 -> user1.age > 10);
//        Optional<User> user3 = Optional.ofNullable(user).ifPresent();
//        Optional<User> user3 = Optional.ofNullable(user).map()；
    }

    public static User createUser() {
        User user = new User();
        user.setName("zhangsan");
        return user;
    }
}
