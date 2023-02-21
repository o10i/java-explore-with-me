package ru.practicum.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StatsServerTest {
    @Test
    void contextLoads() {
        StatsServerApp.main(new String[]{});
    }
}