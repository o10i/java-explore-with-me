package ru.practicum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StatsServerTest {
    @Test
    void contextLoads() {
        StatsServer.main(new String[]{});
    }
}