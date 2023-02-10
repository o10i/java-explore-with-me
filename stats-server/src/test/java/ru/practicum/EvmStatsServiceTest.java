package ru.practicum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EvmStatsServiceTest {
    @Test
    void contextLoads() {
        EvmStatsService.main(new String[]{});
    }
}