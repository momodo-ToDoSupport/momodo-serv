package com.momodo.todohistory;

import com.momodo.todo.Todo;
import com.momodo.todohistory.dto.TodoHistoryRequestDto;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.momodo.todohistory.repository.TodoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoHistoryService {

    private final TodoHistoryRepository todoHistoryRepository;

    private float[] stepRatios = {30.0f, 70.0f, 100.0f};

    @Transactional
    public void create(TodoHistoryRequestDto.Create requestDto){

        int step = calculateStep(requestDto.getCount(), requestDto.getCompletedCount());

        TodoHistory todoHistory = TodoHistory.builder()
                .memberId(requestDto.getMemberId())
                .count(requestDto.getCount())
                .completedCount(requestDto.getCompletedCount())
                .step(step)
                .dueDate(requestDto.getDueDate())
                .build();

        todoHistoryRepository.save(todoHistory);
    }

    public TodoHistoryResponseDto.Info findByDueDate(Long memberId, LocalDate dueDate) {

        return todoHistoryRepository.findByDueDate(memberId, dueDate).toInfo();
    }

    public List<TodoHistoryResponseDto.Info> findAllByYearMonth(Long memberId, String yearMonth) {

        LocalDate firstDate = LocalDate.parse(yearMonth + "-01");
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        return todoHistoryRepository.findAllByYearMonth(memberId, firstDate, lastDate);
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