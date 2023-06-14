package com.finanacialtracing;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthorizeServiceTest {


    @Test
    public void test() {
        String ddd = "asasas";
        Assertions.assertEquals("asasas", ddd);
    }

}
