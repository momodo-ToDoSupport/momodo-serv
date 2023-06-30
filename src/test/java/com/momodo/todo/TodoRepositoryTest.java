package com.momodo.todo;

import com.momodo.TestConfig;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.repository.TodoRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class TodoRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private TodoRepository todoRepository;

    private String memberId = "Test";
    @Test
    @DisplayName("Todo 등록")
    public void create(){
        // given
        Todo todo = createTodo();

        // when
        Todo createdTodo = todoRepository.save(todo);

        // then
        assertThat(todo.getId()).isEqualTo(createdTodo.getId());
        assertThat(todo.getTitle()).isEqualTo(createdTodo.getTitle());
    }

    @Test
    @DisplayName("Todo Id로 조회")
    public void findById(){
        // given
        Todo createdTodo = todoRepository.save(createTodo());

        // when
        Todo foundTodo = todoRepository.findById(createdTodo.getId()).get();

        // then
        assertThat(foundTodo).isNotNull();
        assertThat(foundTodo.getId()).isEqualTo(createdTodo.getId());
    }

    @Test
    @DisplayName("Todo DueDate로 조회")
    public void findAllByDueDate(){
        // given
        List<Todo> createdTodoList = todoRepository.saveAll(createTodoList());

        // when
        List<Todo> todoList = todoRepository.findAllByDueDate(LocalDate.now());

        // then
        assertThat(todoList.size()).isEqualTo(2);
        assertThat(todoList.get(0).getDueDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Todo 성공 여부 수정")
    public void updateCompleted(){
        // given
        Todo createdTodo = todoRepository.save(createTodo());

        // when
        createdTodo.updateCompleted();

        // then
        Todo foundTodo = todoRepository.findById(createdTodo.getId()).get();
        assertThat(foundTodo.isCompleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("Todo 정보 수정")
    public void update(){
        // given
        Todo createdTodo = todoRepository.save(createTodo());
        String editTitle = "Edit Title";
        String editEmoji = "Edit Emoji";
        String editRepeatDays = "Edit RepeatDays";

        // when
        createdTodo.update(editTitle, editEmoji, editRepeatDays);

        // then
        Todo foundTodo = todoRepository.findById(createdTodo.getId()).get();
        assertThat(foundTodo.getTitle()).isEqualTo(editTitle);
        assertThat(foundTodo.getEmoji()).isEqualTo(editEmoji);
        assertThat(foundTodo.getRepeatDays()).isEqualTo(editRepeatDays);
    }

    @Test
    @DisplayName("Todo 삭제")
    public void delete(){
        // given
        Todo createdTodo = todoRepository.save(createTodo());
        Long id = createdTodo.getId();

        // when
        todoRepository.delete(createdTodo);

        // then
        boolean isFound= todoRepository.existsById(id);
        assertThat(isFound).isFalse();
    }

    private Todo createTodo(){
        return Todo.builder()
                .memberId(memberId)
                .title("Test Todo")
                .emoji("Test Emoji")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .repeatDays(null)
                .build();
    }

    private List<Todo> createTodoList(){
        Todo todo1 = new Todo(1L, memberId, "todo1", "emoji1", LocalDate.of(2023,6,2),false,null);
        Todo todo2 = new Todo(2L, memberId, "todo2", "emoji2", LocalDate.of(2023,6,3),false,null);
        Todo todo3 = new Todo(3L, memberId, "todo3", "emoji3", LocalDate.now(),false,null);
        Todo todo4 = new Todo(4L, memberId, "todo4", "emoji4", LocalDate.now(),false,null);
        List<Todo> todos = List.of(
                todo1, todo2, todo3, todo4
        );

        return todos;
    }
}
