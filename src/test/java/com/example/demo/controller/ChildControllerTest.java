package com.example.demo.controller;

import com.example.demo.dto.ChildDto;
import com.example.demo.model.Child;
import com.example.demo.model.Group;
import com.example.demo.model.Relative;
import com.example.demo.repository.ChildRepository;
import com.example.demo.service.ChildService;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@example.com", roles = "USER")
class ChildControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildRepository repository;

    @MockBean
    private ChildService service;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetAll() throws Exception {
        var child1 = new Child("Alice", LocalDate.of(2018, 1, 1));
        var child2 = new Child("Bob", LocalDate.of(2019, 2, 2));
        child1.setId(0L);
        child2.setId(1L);
        var childList = List.of(child1, child2);
        when(repository.findKidsByTeacherEmail(anyString())).thenReturn(childList);

        mockMvc.perform(get("/kindergarten/child"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(child1.getId()))
                .andExpect(jsonPath("$[0].name").value(child1.getName()))
                .andExpect(jsonPath("$[0].birthYear").value(child1.getBirthYear().toString()))
                .andExpect(jsonPath("$[1].id").value(child2.getId()))
                .andExpect(jsonPath("$[1].name").value(child2.getName()))
                .andExpect(jsonPath("$[1].birthYear").value(child2.getBirthYear().toString()));
    }

    @Test
    void testGetFull() throws Exception {
        var child = new Child("John", LocalDate.of(2018, 10, 1));
        var relative1 = new Relative("Mary", "123-456-7890", "123 Main St");
        relative1.setId(0L);
        var relative2 = new Relative("Bob", "987-654-3210", "456 High St");
        relative2.setId(1L);
        child.getRelatives().add(relative1);
        child.getRelatives().add(relative2);

        when(repository.findKidsWithRelativesByTeacherEmail(anyString())).thenReturn(List.of(child));

        mockMvc.perform(get("/kindergarten/child/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].birthYear").value("2018-10-01"))
                .andExpect(jsonPath("$[0].relatives[0].name").value("Mary"))
                .andExpect(jsonPath("$[0].relatives[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[0].relatives[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[0].relatives[1].name").value("Bob"))
                .andExpect(jsonPath("$[0].relatives[1].phone").value("987-654-3210"))
                .andExpect(jsonPath("$[0].relatives[1].address").value("456 High St"));
    }

    @Test
    void testGetChildThatWaitsBirth() throws Exception {
        var child1 = new Child("Alice", LocalDate.of(2022, 4, 1));
        child1.setId(0L);
        var child2 = new Child("Bob", LocalDate.of(2022, 3, 22));
        child2.setId(1L);
        var child3 = new Child("Charlie", LocalDate.of(2022, 3, 21));
        child3.setId(2L);
        when(repository.findKidsThatWaitBirthDay(anyString())).thenReturn(List.of(child1, child2, child3));

        mockMvc.perform(get("/kindergarten/child/birth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value(child1.getName()))
                .andExpect(jsonPath("$[0].birthYear").value(child1.getBirthYear().toString()))
                .andExpect(jsonPath("$[1].name").value(child2.getName()))
                .andExpect(jsonPath("$[1].birthYear").value(child2.getBirthYear().toString()))
                .andExpect(jsonPath("$[2].name").value(child3.getName()))
                .andExpect(jsonPath("$[2].birthYear").value(child3.getBirthYear().toString()));
    }

    @Test
    void testGetBrothersAndSisters() throws Exception {
        var group = new Group("A", 10);
        var child1 = new Child("Alice", LocalDate.of(2018, 1, 1));
        var child2 = new Child("Bob", LocalDate.of(2020, 1, 1));
        var child3 = new Child("Charlie", LocalDate.of(2022, 1, 1));
        child1.setGroup(group);
        child2.setGroup(group);
        child3.setGroup(group);
        when(repository.findBrothersAndSisters(anyLong())).thenReturn(List.of(child2, child3));

        mockMvc.perform(get("/kindergarten/child/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(child2.getName()))
                .andExpect(jsonPath("$[0].birthYear").value(child2.getBirthYear().toString()))
                .andExpect(jsonPath("$[0].groupName").value(group.getName()))
                .andExpect(jsonPath("$[1].name").value(child3.getName()))
                .andExpect(jsonPath("$[1].birthYear").value(child3.getBirthYear().toString()))
                .andExpect(jsonPath("$[1].groupName").value(group.getName()));
    }

    @Test
    void testAddChild() throws Exception {
        var child = new ChildDto(0L, "Alice", LocalDate.of(2019, 5, 1));
        var addedChild = new Child("Alice", LocalDate.of(2019, 5, 1));
        addedChild.setId(0L);
        when(service.add(anyString(), any(Child.class))).thenReturn(addedChild);

        mockMvc.perform(post("/kindergarten/child")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(child)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(addedChild.getName()))
                .andExpect(jsonPath("$.birthYear").value(addedChild.getBirthYear().toString()));
    }

    @Test
    void testUpdateChild() throws Exception {
        var child = new ChildDto(1L, "Alice", LocalDate.of(2019, 5, 1));

        when(repository.updateChild(anyString(), anyLong(), anyString(), any(LocalDate.class))).thenReturn(1);

        mockMvc.perform(patch("/kindergarten/child")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(child)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteChild() throws Exception {
        when(repository.deleteChild(anyString(), anyLong())).thenReturn(1);

        mockMvc.perform(delete("/kindergarten/child/1"))
                .andExpect(status().isOk());
    }
}