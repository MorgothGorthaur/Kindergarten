package com.example.demo.controller;

import com.example.demo.dto.RelativeDto;
import com.example.demo.model.Child;
import com.example.demo.model.Relative;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.RelativeRepository;
import com.example.demo.service.RelativeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@example.com", roles = "USER")
class RelativeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelativeRepository repository;

    @MockBean
    private RelativeService relativeService;

    @MockBean
    private ChildRepository childRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAll() throws Exception {
        var child = new Child("Alice", LocalDate.of(2018, 1, 1));
        child.setId(0L);
        var relative1 = new Relative("John", "123 Main St", "555-1234");
        relative1.setId(0L);
        Relative relative2 = new Relative("Jane", "456 Elm St", "555-5678");
        relative2.setId(1L);
        child.addRelative(relative1);
        child.addRelative(relative2);

        when(childRepository.findChildAndRelativesByIdAndGroup_TeacherEmail(anyLong(), anyString())).thenReturn(Optional.of(child));

        mockMvc.perform(get("/kindergarten/relative/{childId}", child.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(relative1.getName()))
                .andExpect(jsonPath("$[0].address").value(relative1.getAddress()))
                .andExpect(jsonPath("$[0].phone").value(relative1.getPhone()))
                .andExpect(jsonPath("$[1].name").value(relative2.getName()))
                .andExpect(jsonPath("$[1].address").value(relative2.getAddress()))
                .andExpect(jsonPath("$[1].phone").value(relative2.getPhone()));
    }

    @Test
    void testAddRelative() throws Exception {
        var relativeDto = new RelativeDto(0L, "John Doe", "123 Main St", "555-1234");
        var relative = new Relative("John Doe", "123 Main St", "555-1234");
        relative.setId(0L);

        when(relativeService.save(anyString(), anyLong(), anyString(), anyString(), anyString())).thenReturn(relative);
        mockMvc.perform(post("/kindergarten/relative/{childId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(relativeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(relativeDto.name()))
                .andExpect(jsonPath("$.address").value(relativeDto.address()))
                .andExpect(jsonPath("$.phone").value(relativeDto.phone()));
    }

    @Test
    void testDeleteRelative() throws Exception {
        doNothing().when(relativeService).delete(anyString(), anyLong(), anyLong());
        mockMvc.perform(delete("/kindergarten/relative/{childId}/{relativeId}", 1L, 2L))
                .andExpect(status().isOk());

    }

    @Test
    void testUpdateRelative() throws Exception {
        var relativeDto = new RelativeDto(2L, "Jane Doe", "123 Main St", "555-1234");
        var relative = new Relative("Jane Doe", "123 Main St", "555-1234");
        relative.setId(2L);
        when(relativeService.updateOrReplaceRelative(anyString(), anyLong(), anyLong(), anyString(), anyString(), anyString())).thenReturn(relative);
        mockMvc.perform(patch("/kindergarten/relative/{childId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(relativeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(relativeDto.name()))
                .andExpect(jsonPath("$.address").value(relativeDto.address()))
                .andExpect(jsonPath("$.phone").value(relativeDto.phone()));

    }
}