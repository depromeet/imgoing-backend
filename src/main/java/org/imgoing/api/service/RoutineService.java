package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;

    @Transactional(readOnly = true)
    public Routine getById(Long id){
        return routineRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 루틴입니다."));
    }
    @Transactional
    public Routine create(Routine newRoutine){
        return routineRepository.save(newRoutine);
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
