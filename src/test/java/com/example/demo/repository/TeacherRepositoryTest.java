package com.example.demo.repository;

import com.example.demo.model.Child;
import com.example.demo.model.Group;
import com.example.demo.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ChildRepository childRepository;

    @AfterEach
    public void remove() {
        teacherRepository.deleteAll();
        groupRepository.deleteAll();
        childRepository.deleteAll();
    }

    @Test
    void testFindTeacherWithGroupAndKidsByEmail() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        var result = teacherRepository.findTeacherWithGroupAndKidsByEmail(email);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Smith");
        assertThat(result.get().getGroup().getName()).isEqualTo("Group 1");
        assertThat(result.get().getGroup().getKids().size()).isEqualTo(3);
    }


    @Test
    void testFindAllTeachersWithGroups() {
        createTeachersWithGroups();
        var result = teacherRepository.findAllTeachersWithGroups();
        assertThat(result.size()).isEqualTo(3);
    }


    @Test
    void testUpdateTeacherByEmail_shouldReturnOne() {
        createTeacher();
        var email = "john@example.com";
        assertThat(teacherRepository
                .updateTeacherByEmail(email, "new@example.com", "name", "skype", "phone", "password"))
                .isEqualTo(1);
    }

    @Test
    void testUpdateTeacherByEmail_shouldReturnZero() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var teacher2 = new Teacher("Jane Doe", "+0987654321", "jane_skype", "jane@example.com", "password2");
        teacherRepository.saveAll(List.of(teacher1, teacher2));
        var email = "john@example.com";
        var secondEmail = "jane@example.com";
        assertThat(teacherRepository
                .updateTeacherByEmail(email, secondEmail, "name", "skype", "phone", "password"))
                .isEqualTo(0);
    }

    @Test
    void testDeleteTeacherByEmail_shouldReturnOne() {
        createTeacher();
        var email = "john@example.com";
        assertThat(teacherRepository.deleteTeacherByEmail(email)).isEqualTo(1);
    }

    @Test
    void testDeleteTeacherByEmail_shouldReturnZero() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        assertThat(teacherRepository.deleteTeacherByEmail(email)).isEqualTo(0);
    }

    public void createTeacherWithGroupAndKids() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var group1 = new Group("Group 1", 3);
        teacher1.addGroup(group1);
        var child1 = childRepository.save(new Child("Child 1", LocalDate.now().minusYears(1)));
        var child2 = childRepository.save(new Child("Child 2", LocalDate.now().minusYears(1).plusMonths(5)));
        var child3 = childRepository.save(new Child("Child 3", LocalDate.now().minusYears(3)));
        group1.addChild(child1);
        group1.addChild(child2);
        group1.addChild(child3);
        teacherRepository.save(teacher1);
    }

    public void createTeachersWithGroups() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var teacher2 = new Teacher("Jane Doe", "+0987654321", "jane_skype", "jane@example.com", "password2");
        var teacher3 = new Teacher("Bob Johnson", "+1111111111", "bob_skype", "bob@example.com", "password3");

        var group1 = new Group("Group 1", 3);
        var group2 = new Group("Group 2", 4);
        var group3 = new Group("Group 3", 2);
        teacher1.addGroup(group1);
        teacher2.addGroup(group2);
        teacher3.setGroup(group3);
        teacherRepository.saveAll(List.of(teacher1, teacher2, teacher3));
    }

    public void createTeacher() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        teacherRepository.save(teacher1);
    }

}