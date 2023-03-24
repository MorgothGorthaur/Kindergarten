package com.example.demo.repository;

import com.example.demo.model.Child;
import com.example.demo.model.Group;
import com.example.demo.model.Relative;
import com.example.demo.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

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

    @BeforeEach
    public void setUp() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var teacher2 = new Teacher("Jane Doe", "+0987654321", "jane_skype", "jane@example.com", "password2");
        var teacher3 = new Teacher("Bob Johnson", "+1111111111", "bob_skype", "bob@example.com", "password3");

        var group1 = new Group("Group 1", 3);
        var group2 = new Group("Group 2", 4);
        var group3 = new Group("Group 3", 2);

        var relative1 = relativeRepository.save(new Relative("John's relative", "+1234567890", "John's relative address"));
        var relative2 = relativeRepository.save(new Relative("Jane's relative", "+0987654321", "Jane's relative address"));
        var child1 = childRepository.save(new Child("Child 1", LocalDate.of(2015, 1, 1),
                relative1));
        var child2 = childRepository.save(new Child("Child 2", LocalDate.of(2016, 2, 2),
                relative1));
        var child3 = childRepository.save(new Child("Child 3", LocalDate.of(2017, 3, 3),
                relative2));
        var child4 = childRepository.save(new Child("Child 4", LocalDate.of(2018, 4, 4),
                relative2));
        var child5 = childRepository.save(new Child("Child 5", LocalDate.of(2019, 5, 5),
                relative2));
        child5.addRelative(relative1);

        group1.addChild(child1);
        group1.addChild(child2);
        group1.addChild(child3);

        group2.addChild(child4);
        group2.addChild(child5);

        teacher1.addGroup(group1);
        teacher2.addGroup(group2);
        teacher3.setGroup(group3);
        teacherRepository.saveAll(List.of(teacher1, teacher2, teacher3));
    }

    @AfterEach
    public void remove() {
        teacherRepository.deleteAll();
        groupRepository.deleteAll();
        childRepository.deleteAll();
        relativeRepository.deleteAll();
    }


    @Test
    void testFindRelativeByIdAndChildIdAndTeacherEmail() {
        var email = "john@example.com";
        var childId = childRepository.findAll().get(0).getId();
        var relativeId = relativeRepository.findAll().get(0).getId();
        var result = relativeRepository.findRelativeByIdAndChildIdAndTeacherEmail(relativeId, childId, email);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(relativeId);
    }

    @Test
    void testFindRelativesByChildIdAndTeacherEmail() {
        var email = "john@example.com";
        var childId = childRepository.findAll().get(0).getId();
        assertThat(relativeRepository.findRelativesByChildIdAndTeacherEmail(childId, email).size()).isEqualTo(1);
    }

    @Test
    void testFindEqualRelative() {
        assertThat(relativeRepository.findEqualRelative("John's relative", "John's relative address", "+1234567890")).isPresent();
    }

    @Test
    void testFindEqualRelativeWithAnotherId() {
        assertThat(relativeRepository.findEqualRelativeWithAnotherId("John's relative", "John's relative address", "+1234567890", 3L)).isPresent();
    }

}