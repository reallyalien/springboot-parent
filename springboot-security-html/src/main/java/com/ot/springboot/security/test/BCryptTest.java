package com.ot.springboot.security.test;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptTest {

    public static void main(String[] args) {
        String str="123456";
        String hashpw = BCrypt.hashpw(str, BCrypt.gensalt());
        System.out.println(hashpw);//$2a$10$8Ca8LLK.z7wJQANHenA6i.loVc5qpxe83SNTeqmG/Mt8C9GynFDsC
    }
}
