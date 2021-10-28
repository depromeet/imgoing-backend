package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routine;
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
        newRoutine.registerRoutinetasks(taskService.getListById(taskIdList));
        routinetaskService.createAll(newRoutine.getRoutinetasks());
        return routineRepository.save(newRoutine);
    }

    @Transactional(readOnly = true)
    public Routine getById(Long id){
        return routineRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 루틴입니다."));
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
    public Routine update(Routine newRoutine){
        Long id = newRoutine.getId();
        Routine oldRoutine = getById(id);

        oldRoutine.modifyRoutine(newRoutine);
        return routineRepository.save(oldRoutine);
    }
}