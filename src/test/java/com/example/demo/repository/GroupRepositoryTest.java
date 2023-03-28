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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class GroupRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private RelativeRepository relativeRepository;


    @AfterEach
    public void remove() {
        teacherRepository.deleteAll();
        groupRepository.deleteAll();
        childRepository.deleteAll();
        relativeRepository.deleteAll();
    }

    @Test
    void testDeleteGroupIfEmptyByTeacherEmail_shouldReturnOne() {
        createTeacherWithGroup();
        var email = "john@example.com";
        assertThat(groupRepository.deleteGroupIfEmptyByTeacherEmail(email)).isEqualTo(1);
    }

    @Test
    void testDeleteGroupIfEmptyByTeacherEmail_shouldReturnZero() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        assertThat(groupRepository.deleteGroupIfEmptyByTeacherEmail(email)).isEqualTo(0);
    }

    @Test
    void testUpdateGroupByTeacherEmail_shouldReturnOne() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        assertThat(groupRepository.updateGroupByTeacherEmail(email, "name", 5)).isEqualTo(1);
    }

    @Test
    void testUpdateGroupByTeacherEmail_shouldReturnZero() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        assertThat(groupRepository.updateGroupByTeacherEmail(email, "name", 1)).isEqualTo(0);
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

    private void createTeacherWithGroup() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var group1 = new Group("Group 1", 3);
        teacher1.addGroup(group1);
        teacherRepository.save(teacher1);
    }

}