package com.example.demo.repository;

import com.example.demo.exception.ChildNotFoundException;
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
    void testFindKidsByTeacherEmail() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        assertThat(childRepository.findKidsByTeacherEmail(email).size()).isEqualTo(3);
    }

    @Test
    void testFindKidsWithRelativesByTeacherEmail() {
        createTeacherWithGroupAndKidsAndRelatives();
        var email = "john@example.com";
        var result = childRepository.findKidsWithRelativesByTeacherEmail(email);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getRelatives().size()).isEqualTo(1);
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

    @Test
    void testFindChildWithRelativesByIdAndTeacherEmail() {
        createTeacherWithGroupAndKidsAndRelatives();
        var email = "john@example.com";
        var kids = childRepository.findAll();
        var result = childRepository.findChildWithRelativesByIdAndTeacherEmail(kids.get(0).getId(), email)
                .orElseThrow(() -> new ChildNotFoundException(email));
        assertThat(result.getName()).isEqualTo(kids.get(0).getName());
        assertThat(result.getRelatives().size()).isEqualTo(1);
    }

    @Test
    void testUpdateChildByIdAndTeacherEmail_shouldReturnOne() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        var kids = childRepository.findAll();
        assertThat(childRepository.updateChildByIdAndTeacherEmail(email, kids.get(0).getId(), "name", LocalDate.now())).isEqualTo(1);
    }

    @Test
    void testUpdateChildByIdAndTeacherEmail_shouldReturnZero() {
        createTeacherWithGroupAndKids();
        var email = "johfffffffffffn@example.com";
        var kids = childRepository.findAll();
        assertThat(childRepository.updateChildByIdAndTeacherEmail(email, kids.get(0).getId(), "name", LocalDate.now())).isEqualTo(0);
    }

    @Test
    public void testDeleteChildByIdAndTeacherEmail_shouldReturnOne() {
        createTeacherWithGroupAndKids();
        var email = "john@example.com";
        var kids = childRepository.findAll();
        var id = kids.get(0).getId();
        var res = childRepository.deleteChildByIdAndTeacherEmail(email, id);
        assertThat(res).isEqualTo(1);
    }

    @Test
    void testDeleteChildByIdAndTeacherEmail_shouldReturnZero() {
        createTeacherWithGroupAndKids();
        var email = "johfffffffffffn@example.com";
        var kids = childRepository.findAll();
        assertThat(childRepository.deleteChildByIdAndTeacherEmail(email, kids.get(0).getId())).isEqualTo(0);
    }

    public void createTeacherWithGroupAndKids() {
        var teacher1 = new Teacher("John Smith", "+1234567890", "john_skype", "john@example.com", "password1");
        var group1 = new Group("Group 1", 3);
        teacher1.addGroup(group1);
        var child1 = childRepository.save(new Child("Child 1", LocalDate.of(2015, 1, 1)));
        var child2 = childRepository.save(new Child("Child 2", LocalDate.of(2016, 2, 2)));
        var child3 = childRepository.save(new Child("Child 3", LocalDate.of(2017, 3, 3)));
        group1.addChild(child1);
        group1.addChild(child2);
        group1.addChild(child3);
        teacherRepository.save(teacher1);
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