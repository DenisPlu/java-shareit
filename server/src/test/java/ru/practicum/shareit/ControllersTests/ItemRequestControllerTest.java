package ru.practicum.shareit.ControllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private ItemRequestServiceImpl itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;
    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();

        itemRequestDto = new ItemRequestDto(
                1L,
                "Запрос дрели",
                1L,
                LocalDateTime.now(),
                List.of(new ItemDtoForRequest(1L, "Дрель #1", "Автоматическая дрель", true, 1L))
        );

        itemRequest = new ItemRequest(
                1L,
                "Запрос дрели",
                1L,
                LocalDateTime.now()
        );
    }

    @Test
    void create() throws Exception {
        when(itemRequestService.create(any(), anyLong()))
                .thenReturn(itemRequest);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.requesterId", is(itemRequest.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.created[0]", is(itemRequest.getCreated().getYear())))
                .andExpect(jsonPath("$.created[1]", is(itemRequest.getCreated().getMonth().getValue())))
                .andExpect(jsonPath("$.created[2]", is(itemRequest.getCreated().getDayOfMonth())))
                .andExpect(jsonPath("$.created[3]", is(itemRequest.getCreated().getHour())));
    }

    @Test
    void getAllByOwner() throws Exception {
        when(itemRequestService.getAllByOwner(any()))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0]description", is(itemRequestDto.getDescription()),String.class))
                .andExpect(jsonPath("$.[0]requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.[0]created[0]", is(itemRequestDto.getCreated().getYear()), int.class))
                .andExpect(jsonPath("$.[0]created[1]", is(itemRequestDto.getCreated().getMonth().getValue()), int.class))
                .andExpect(jsonPath("$.[0]created[2]", is(itemRequestDto.getCreated().getDayOfMonth()), int.class))
                .andExpect(jsonPath("$.[0]created[3]", is(itemRequestDto.getCreated().getHour()), int.class))
                .andExpect(jsonPath("$.[0]items[0]description", is(itemRequestDto.getItems().get(0).getDescription()), String.class));
    }

    @Test
    void getAll() throws Exception {
        when(itemRequestService.getAll(anyInt(), anyLong(), anyString()))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0]description", is(itemRequestDto.getDescription()),String.class))
                .andExpect(jsonPath("$.[0]requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.[0]created[0]", is(itemRequestDto.getCreated().getYear()), int.class))
                .andExpect(jsonPath("$.[0]created[1]", is(itemRequestDto.getCreated().getMonth().getValue()), int.class))
                .andExpect(jsonPath("$.[0]created[2]", is(itemRequestDto.getCreated().getDayOfMonth()), int.class))
                .andExpect(jsonPath("$.[0]created[3]", is(itemRequestDto.getCreated().getHour()), int.class))
                .andExpect(jsonPath("$.[0]items[0]description", is(itemRequestDto.getItems().get(0).getDescription()), String.class));
    }

    @Test
    void getByRequestId() throws Exception {
        when(itemRequestService.getByRequestId(any(), any()))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()),String.class))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.created[0]", is(itemRequestDto.getCreated().getYear()), int.class))
                .andExpect(jsonPath("$.created[1]", is(itemRequestDto.getCreated().getMonth().getValue()), int.class))
                .andExpect(jsonPath("$.created[2]", is(itemRequestDto.getCreated().getDayOfMonth()), int.class))
                .andExpect(jsonPath("$.created[3]", is(itemRequestDto.getCreated().getHour()), int.class))
                .andExpect(jsonPath("$.items[0]description", is(itemRequestDto.getItems().get(0).getDescription()), String.class));
    }
}
