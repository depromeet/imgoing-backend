package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.repository.RoutineRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final TaskService taskService;
    private final RoutinetaskService routinetaskService;
    private final RoutineRepository routineRepository;

    @Transactional
    public Routine create(Routine newRoutine, List<Long> taskIdList){
        if(taskIdList.isEmpty()) throw new ImgoingException(ImgoingError.BAD_REQUEST, "루틴에는 최소 하나 이상의 준비항목이 포함되어야 합니다.");
        newRoutine.registerRoutinetasks(taskService.getListById(taskIdList));
        routinetaskService.createAll(newRoutine.getRoutinetasks());
        return routineRepository.save(newRoutine);
    }

    @Transactional(readOnly = true)
    public List<Routine> getListByUserId(Long userId){
        return routineRepository.findAllByUserId(userId);
    }

    @Transactional
    public void delete(Routine routine){
        routineRepository.delete(routine);
    }

    @Transactional
    public Routine modify(Routine oldRoutine, Routine newRoutine, List<Long> updateTaskIdList){ // mapper에 의해 새로 생성된 Routine
        oldRoutine.modifyRoutine(newRoutine);
        routineRepository.save(oldRoutine);

        List<Routinetask> routinetasks = routinetaskService.update(newRoutine, updateTaskIdList);

        return Routine.builder()
                .id(newRoutine.getId())
                .name(newRoutine.getName())
                .user(newRoutine.getUser())
                .routinetasks(routinetasks)
                .build();
    }
}