package com.example.demo.repository;

import com.example.demo.model.Child;
import com.example.demo.model.Group;
import com.example.demo.model.Relative;
import com.example.demo.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class RelativeRepositoryTest {
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
        childRepository.deleteAll();
        relativeRepository.deleteAll();
        groupRepository.deleteAll();
        teacherRepository.deleteAll();
    }


    @Test
    void testFindRelativeWithChild() {
        createTeacherWithGroupAndKidsAndRelatives();
        var email = teacherRepository.findAll().get(0).getEmail();
        var childId = childRepository.findAll().get(0).getId();
        var relativeId = relativeRepository.findAll().get(0).getId();
        var result = relativeRepository.findRelativeWithChild(relativeId, childId, email);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(relativeId);
        assertThat(result.get().getKids().stream().toList().get(0)).isNotNull();
    }

    public void createTeacherWithGroupAndKidsAndRelatives() {
        var relative1 = relativeRepository.save(new Relative("John's relative", "+1234567890", "John's relative address"));
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var group1 = new Group("Group 1", 3);
        teacher1.addGroup(group1);
        var child1 = childRepository.save(new Child("Child 1", LocalDate.now().minusYears(1)));
        child1.addRelative(relative1);
        group1.addChild(child1);
        teacherRepository.save(teacher1);
    }
}