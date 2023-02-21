package ru.practicum.ewm.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static ru.practicum.ewm.common.mapper.DateTimeMapper.toStringDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    public static EndpointHit toEndpointHit(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp("ewm-service");
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setTimestamp(toStringDateTime(LocalDateTime.now()));
        return endpointHit;
    }
}
