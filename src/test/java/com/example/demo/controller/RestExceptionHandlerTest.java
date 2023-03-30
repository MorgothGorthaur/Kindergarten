package com.example.demo.controller;

import com.example.demo.dto.ChildDto;
import com.example.demo.dto.GroupDto;
import com.example.demo.exception.TeacherAlreadyContainsGroupException;
import com.example.demo.model.Group;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@example.com", roles = "USER")
class RestExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TeacherRepository teacherRepository;
    @MockBean
    GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHandleEntityNotFoundEx() throws Exception {
        mockMvc.perform(get("/kindergarten/teacher"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("entity not found exception"))
                .andExpect(jsonPath("$.debugMessage").value("teacher with email = test@example.com not founded!"));
    }

    @Test
    void testHandleDataNotAcceptableEx() throws Exception {
        var group = new GroupDto("group", 1);
        doThrow(new TeacherAlreadyContainsGroupException("test@example.com")).when(groupService).add(anyString(), any(Group.class));
        mockMvc.perform(post("/kindergarten/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value("This data is not acceptable!"))
                .andExpect(jsonPath("$.debugMessage").value("teacher with email test@example.com already contains group"));
    }

    @Test
    void testHandleInvalidArgument() throws Exception {
        var child = new ChildDto(0L, "johny", LocalDate.now().plusYears(1));
        mockMvc.perform(post("/kindergarten/child")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(child)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("validation error"))
                .andExpect(jsonPath("$.debugMessage").value("Birth year must be before today`s date!"));
    }

    @Test
    void testHandleHttpMessageNotReadable() throws Exception {
        mockMvc.perform(post("/kindergarten/child")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON Request"));
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/kindergarten/child/{id}", "abracadabra"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("bad argument type"));
    }

}