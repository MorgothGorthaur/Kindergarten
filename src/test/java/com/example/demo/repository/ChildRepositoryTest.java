package com.example.demo.repository;

import com.example.demo.exception.ChildNotFoundException;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ChildRepositoryTest {
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
    void testFindKidsByTeacherEmail() {
        var email = "john@example.com";
        assertThat(childRepository.findKidsByTeacherEmail(email).size()).isEqualTo(3);
    }

    @Test
    void testFindKidsWithRelativesByTeacherEmail() {
        var email = "jane@example.com";
        var result = childRepository.findKidsByTeacherEmail(email);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(1).getRelatives().size()).isEqualTo(2);
    }


    @Test
    void testFindBrothersAndSisters() {
        var email = "john@example.com";
        var kids = childRepository.findAll();
        var result = childRepository.findBrothersAndSisters(kids.get(0).getId());
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void testFindChildWithRelativesByIdAndTeacherEmail() {
        var email = "john@example.com";
        var kids = childRepository.findAll();
        var result = childRepository.findChildWithRelativesByIdAndTeacherEmail(kids.get(0).getId(), email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        assertThat(result.getName()).isEqualTo(kids.get(0).getName());
    }

    @Test
    void testUpdateChild_shouldReturnOne() {
        var email = "john@example.com";
        var kids = childRepository.findAll();
        assertThat(childRepository.updateChild(email, kids.get(0).getId(), "name", LocalDate.now())).isEqualTo(1);

    }

    @Test
    void testUpdateChild_shouldReturnZero() {
        var email = "johfffffffffffn@example.com";
        var kids = childRepository.findAll();
        assertThat(childRepository.updateChild(email, kids.get(0).getId(), "name", LocalDate.now())).isEqualTo(0);

    }

    @Test
    @Transactional
    void testDeleteChild_shouldReturnOne() {
        var email = "john@example.com";
        var id = childRepository.findAll().get(0).getId();
        //  childRepository.deleteChild(email, id);
        delete(email, id);
    }

    @Transactional
    int delete(String email, long id) {
        return childRepository.deleteChild(email, id);
    }

    @Test
    void testDeleteChild_shouldReturnZero() {
        var email = "johfffffffffffn@example.com";
        var kids = childRepository.findAll();
        assertThat(childRepository.deleteChild(email, kids.get(0).getId())).isEqualTo(0);
    }
}