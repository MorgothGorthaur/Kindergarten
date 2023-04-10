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

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class ChildRepositoryTest {
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
    void testRelatedKidsWithTheirGroupsAndTeachers() {
        createTeacherWithGroupAndKidsAndRelatives();
        var kids = childRepository.findAll();
        var result = childRepository.findRelatedKidsWithTheirGroupsAndTeachers(kids.get(0).getId());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getGroup()).isNotNull();
        assertThat(result.get(0).getGroup().getTeacher()).isNotNull();
    }

    public void createTeacherWithGroupAndKidsAndRelatives() {
        var relative1 = relativeRepository.save(new Relative("John's relative", "+1234567890", "John's relative address"));
        var relative2 = relativeRepository.save(new Relative("Jane's relative", "+0987654321", "Jane's relative address"));
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var group1 = new Group("Group 1", 3);
        teacher1.addGroup(group1);
        var child1 = childRepository.save(new Child("Child 1", LocalDate.now().minusYears(1)));
        child1.addRelative(relative1);
        var child2 = childRepository.save(new Child("Child 2", LocalDate.now().minusYears(1).plusMonths(5)));
        child2.addRelative(relative1);
        var child3 = childRepository.save(new Child("Child 3", LocalDate.now().minusYears(3)));
        child3.addRelative(relative2);
        group1.addChild(child1);
        group1.addChild(child2);
        group1.addChild(child3);
        teacherRepository.save(teacher1);

    }
}