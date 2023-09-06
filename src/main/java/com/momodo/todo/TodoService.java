package com.momodo.todo;

import com.momodo.emojihistory.EmojiHistoryService;
import com.momodo.jwt.exception.error.NotFoundException;
import com.momodo.todo.Todo;
import com.momodo.todo.dto.TodoRequestDto;
import com.momodo.todo.dto.TodoResponseDto;
import com.momodo.todo.event.TodoCreatedEvent;
import com.momodo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void create(TodoRequestDto.Create request, String memberId){
        Todo todo = request.toEntity();
        todo.setMemberId(memberId);

        // 반복일 설정이 되어 있다면, 해당되는 날짜의 todo들을 생성해 함께 저장
        if(!(request.getRepeatDays().isBlank() && request.getDuration() == 0)){

            List<Todo> createTodos = createTodosByDates(todo, request.getRepeatDays(), request.getDuration(), memberId);
            createTodos.add(0, todo);
            todoRepository.saveTodos(createTodos);
        }
        // 단일 저장
        else{
            todoRepository.save(todo);
        }

        publisher.publishEvent(new TodoCreatedEvent(memberId, request.getEmoji()));
    }

    private List<Todo> createTodosByDates(Todo todo, String repeatDays, Integer duration, String memberId){
        List<LocalDate> createDates = calculateDaysOfWeekBetweenTwoDates(todo.getDueDate(), repeatDays, duration);
        List<Todo> createTodoList = createDates.stream()
                .map(date -> {
                    return Todo.builder()
                            .memberId(memberId)
                            .title(todo.getTitle())
                            .emoji(todo.getEmoji())
                            .dueDate(date)
                            .isCompleted(false)
                            .build();
                })
                .collect(Collectors.toList());

        return createTodoList;
    }

    @Transactional(readOnly = true)
    public TodoResponseDto.Info findById(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        return todo.toInfo();
    }

    @Transactional(readOnly = true)
    public List<Todo> findAllByDueDate(LocalDate dueDate){
        List<Todo> todoList = todoRepository.findAllByDueDate(dueDate);

        return todoList;
    }

    public List<TodoResponseDto.Info> findByMemberAndDueDate(String memberId, LocalDate dueDate){
        return todoRepository.findByMemberAndDueDate(memberId, dueDate);
    }

    @Transactional(readOnly = true)
    public List<TodoResponseDto.Info> findNotCompleteInYearMonth(String memberId, String yearMonth){

        LocalDate firstDate = LocalDate.parse(yearMonth + "-01");
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        return todoRepository.findNotCompleteInYearMonth(memberId, firstDate, lastDate);
    }

    @Transactional
    public void updateCompleted(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        todo.updateCompleted();
    }

    @Transactional
    public void update(Long id, TodoRequestDto.Edit request){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        if(request.getEmoji() != todo.getEmoji()){
            publisher.publishEvent(new TodoCreatedEvent(todo.getMemberId(), request.getEmoji()));
        }

        todo.update(request.getTitle(), request.getEmoji());

        // 반복일 설정이 되어 있다면, 해당되는 날짜의 todo들을 생성해 저장
        if(!(request.getRepeatDays().isBlank() && request.getDuration() == 0)){
            List<Todo> createTodos = createTodosByDates(todo, request.getRepeatDays(), request.getDuration(), todo.getMemberId());
            todoRepository.saveTodos(createTodos);
        }
    }

    @Transactional
    public void deleteById(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        todoRepository.delete(todo);
    }

    // 특정한 두 날짜 사이에 요일별 날짜 구하기
    public List<LocalDate> calculateDaysOfWeekBetweenTwoDates(LocalDate dueDate, String repeatDays, Integer duration){
        /**
         * 시작 날짜와 종료 날짜 설정
         * 시작 날짜는 dueDate 다음날부터
         * 종료 날짜는 duration - 1로 설정
          */
        LocalDate startDate = dueDate.plusDays(1);
        LocalDate endDate = dueDate.plusDays(duration - 1);

        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        /**
         * 종료 날짜와 같아질 때까지 반복
         * currentDate 날짜의 요일이 설정한 반복 요일일 경우에 리스트에 추가
          */
        while(currentDate.compareTo(endDate) <= 0){
            String dayOfWeek = String.valueOf(currentDate.getDayOfWeek().getValue());
            if(repeatDays.contains(dayOfWeek)){
                dates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }
}
