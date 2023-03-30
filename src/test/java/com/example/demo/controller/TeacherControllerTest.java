package com.example.demo.controller;

import com.example.demo.dto.TeacherFullDto;
import com.example.demo.dto.TeacherWithGroupDto;
import com.example.demo.model.Group;
import com.example.demo.model.Teacher;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TeacherService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testAdd() throws Exception {
        var teacherDto = new TeacherFullDto("John", "1234567", "john_skype", "john@example.com", "password");
        doNothing().when(teacherService).save(any(Teacher.class));

        mockMvc.perform(post("/kindergarten/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll() throws Exception {
        var group1 = new Group("A", 10);
        var group2 = new Group("B", 15);

        var teacher1 = new Teacher("John", "1234567", "john_skype", "john@example.com", "password");
        teacher1.setGroup(group1);

        var teacher2 = new Teacher("Mary", "7654321", "mary_skype", "mary@example.com", "password");
        teacher2.setGroup(group2);
        var teacherWithGroupDto1 = new TeacherWithGroupDto(teacher1);
        var teacherWithGroupDto2 = new TeacherWithGroupDto(teacher2);
        var teacherList = Arrays.asList(teacher1, teacher2);
        var teacherWithGroupDtoList = Arrays.asList(teacherWithGroupDto1, teacherWithGroupDto2);

        when(teacherRepository.findAllTeachersWithGroups()).thenReturn(teacherList);

        mockMvc.perform(get("/kindergarten/teacher/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(teacherWithGroupDtoList)));

    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testGet() throws Exception {
        var teacher = new Teacher("John", "1234567", "john_skype", "john@example.com", "password");
        when(teacherRepository.findTeacherByEmail("john@example.com")).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/kindergarten/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void testUpdate() throws Exception {
        var dto = new TeacherFullDto("John", "1234567", "john_skype", "john@example.com", "new_password");

        when(teacherRepository.updateTeacherByEmail(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(1);

        mockMvc.perform(patch("/kindergarten/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void testRemove() throws Exception {
        when(teacherRepository.deleteTeacherByEmail(anyString())).thenReturn(1);

        mockMvc.perform(delete("/kindergarten/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}