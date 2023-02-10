package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    StatsService service;
    @Autowired
    MockMvc mvc;

    @SneakyThrows
    @Test
    void save() {
        HitDto hitDto = new HitDto("ewm-main-service", "/events/1", "73.80.0.87", "2023-02-09 13:50:47");

        mvc.perform(MockMvcRequestBuilders.post("/hit")
                        .content(mapper.writeValueAsString(hitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    void getStats() {
        ViewStats viewStats = new ViewStats("ewm-main-service", "/events/1", 1L);
        when(service.getStats(any(), any(), any(), any())).thenReturn(List.of(viewStats));

        mvc.perform(get("/stats")
                        .param("start", "2023-02-09 13:50:47")
                        .param("end", "2023-02-09 13:50:47")
                        .param("uris", List.of().toString())
                        .param("unique", "FALSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].app", is(viewStats.getApp())))
                .andExpect(jsonPath("$[0].uri", is(viewStats.getUri())))
                .andExpect(jsonPath("$[0].hits", is(1)));
    }
}
