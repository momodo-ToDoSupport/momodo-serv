package com.momodo.todolist;

import com.momodo.todo.Todo;
import com.momodo.todolist.dto.TodoListResponseDto;
import com.momodo.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;

    private float[] stepRatios = {30.0f, 70.0f, 100.0f};

    @Transactional
    public void create(Long memberId, LocalDate dueDate){

        TodoList todoList = TodoList.builder()
                .memberId(memberId)
                .count(1L)
                .completedCount(0L)
                .step(0)
                .dueDate(dueDate)
                .build();

        todoListRepository.save(todoList);
    }

    public TodoListResponseDto.Info findByDueDate(Long memberId, LocalDate dueDate) {

        return todoListRepository.findByDueDate(memberId, dueDate).toInfo();
    }

    public List<TodoListResponseDto.Info> findAllByYearMonth(Long memberId, String yearMonth) {

        LocalDate firstDate = LocalDate.parse(yearMonth + "-01");
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        return todoListRepository.findAllByYearMonth(memberId, firstDate, lastDate);
    }

    @Transactional
    public void updateTodoCount(Todo todo, boolean isCreated){ // TodoList의 Todo 개수 수정

        TodoList todoList = todoListRepository.findByDueDate(todo.getMemberId(), todo.getDueDate());
        long todoCount = 0;
        long todoCompletedCount = 0;
        int step = 0;

        if(isCreated){  // 새로운 Todo 생성
            if(todoList == null){
                create(todo.getMemberId(), todo.getDueDate());
            }else{
                todoCount = todoList.getCount() + 1;
                todoCompletedCount = todoList.getCompletedCount();
                step = calculateStep(todoCount, todoCompletedCount);

                todoList.update(todoCount, todoCompletedCount, step);
            }
        }else{  // Todo 삭제
            // 존재하지 않는 memberId일 경우를 체크해야 하나?
            todoCount = todoList.getCount() - 1;

            if(todoCount == 0){ // Todo 개수가 0개면 TodoList 삭제

                todoListRepository.delete(todoList);
                return;
            }
            if(todo.isCompleted()){ // 삭제하려는 Todo가 완료한 Todo라면
                todoCompletedCount = todoList.getCompletedCount() - 1;
            }

            step = calculateStep(todoCount, todoCompletedCount);
            todoList.update(todoCount, todoCompletedCount, step);
        }
    }


    @Transactional
    public void updateTodoCompleted(Todo todo){

        TodoList todoList = todoListRepository.findByDueDate(todo.getMemberId(), todo.getDueDate());
        // 나중에 예외를 추가해야 할까? 어차피 TodoList가 존재하기 때문에 이 메서드가 실행 가능한데?

        long todoCount = todoList.getCount();
        long todoCompletedCount = todoList.getCompletedCount();
        int step = 0;
        
        if(todo.isCompleted()){ // Todo 미완료 -> 완료
            todoCompletedCount += 1;
        }else{ // Todo 완료 -> 미완료
            todoCompletedCount -= 1;
        }

        step = calculateStep(todoCount, todoCompletedCount);
        todoList.update(todoCount, todoCompletedCount, step);
    }

    private int calculateStep(long todoCount, long todoCompletedCount){

        float ratio = ((float)todoCompletedCount / todoCount) * 100;
        int step = 0;

        for(int i = 0; i < stepRatios.length; i++){

            if(ratio == stepRatios[i]){ // 비율이 딱 맞아 떨어지면
                step = i + 1;
            }
            else if(ratio < stepRatios[i]){  // 미만이면 현재 단계 유지
                break;
            }else{                      // 이상이면 +1
                step += 1;
            }
        }

        return step;
    }
}
