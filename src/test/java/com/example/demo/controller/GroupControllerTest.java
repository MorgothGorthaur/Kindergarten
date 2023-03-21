package com.example.demo.controller;

import com.example.demo.dto.GroupDto;
import com.example.demo.model.Group;
import com.example.demo.model.Teacher;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.GroupService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@example.com", roles = "USER")
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private GroupService groupService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetGroup() throws Exception {
        var group = new Group("A", 10);
        var teacher = new Teacher("John", "1234567", "john_skype", "john@example.com", "password");
        teacher.setGroup(group);
        when(teacherRepository.findTeacherWithGroupAndKidsByEmail(anyString())).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/kindergarten/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(group.getName()))
                .andExpect(jsonPath("$.maxSize").value(group.getMaxSize()))
                .andExpect(jsonPath("$.currentSize").value(0));
    }


    @Test
    void testAdd() throws Exception {
        var dto = new GroupDto("Test Group", 5);

        doNothing().when(groupService).add(anyString(), any(Group.class));
        mockMvc.perform(post("/kindergarten/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @Test
    void testUpdate() throws Exception {
        GroupDto dto = new GroupDto("New Group Name", 20);

        when(groupRepository.updateGroup(anyString(), anyString(), anyInt())).thenReturn(1);

        mockMvc.perform(patch("/kindergarten/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemove() throws Exception {
        when(groupRepository.deleteGroupFromTeacherIfGroupDoesntContainsKids(anyString())).thenReturn(1);

        mockMvc.perform(delete("/kindergarten/group"))
                .andExpect(status().isOk());
    }
}