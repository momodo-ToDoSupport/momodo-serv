package com.momodo.todohistory;

import com.momodo.todohistory.domain.TodoHistory;
import com.momodo.todohistory.domain.TodoTier;
import com.momodo.todohistory.dto.TodoHistoryResponseDto;
import com.momodo.todohistory.repository.TodoHistoryRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.momodo.todohistory.domain.TodoTier.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoHistoryService {

    private final TodoHistoryRepository todoHistoryRepository;

    private float[] stepRatios = {30.0f, 70.0f, 100.0f};

    @Transactional
    public void createAll(List<TodoHistory> saveList){

        todoHistoryRepository.saveAll(saveList);
    }

    public TodoHistoryResponseDto.Info findByDueDate(String memberId, LocalDate dueDate) {

        return todoHistoryRepository.findByDueDate(memberId, dueDate).toInfo();
    }

    public List<TodoHistoryResponseDto.Info> findAllByYearMonth(String memberId, String yearMonth) {

        LocalDate firstDate = LocalDate.parse(yearMonth + "-01");
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        return todoHistoryRepository.findAllByYearMonth(memberId, firstDate, lastDate);
    }

    public int calculateStep(long todoCount, long todoCompletedCount){

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

    /**
     * 월 1일 부터 beforeDate까지
     * 유저별 2단계 이상 달성한 TodoHistory 개수 반환
     */
    public Map<String, Integer> countBySecondStepAchievement(LocalDate date){
        LocalDate firstDate = LocalDate.of(date.getYear(), date.getMonth(), 1);

        return todoHistoryRepository.countBySecondStepAchievement(firstDate, date);
    }

    public TodoTier calculateTodoTier(int countBySecondStepAchievement) {

        // 반환된 개수에 따라 티어 반환
        if (countBySecondStepAchievement < GREEN.getTier())
            return RED;
        else if (countBySecondStepAchievement < BLUE.getTier())
            return GREEN;
        else if (countBySecondStepAchievement < RAINBOW.getTier())
            return BLUE;
        else
            return RAINBOW;
    }
}
