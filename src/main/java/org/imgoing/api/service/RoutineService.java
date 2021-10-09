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
    private final RoutineRepository routineRepository;

    @Transactional
    public Routine create(Routine newRoutine){
        return routineRepository.save(newRoutine);
    }

    @Transactional(readOnly = true)
    public Routine getById(Long id){
        return routineRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.ROUTINE_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public List<Routine> getListAll(){
        return routineRepository.findAll();
    }

    @Transactional
    public Routine update(Routine newRoutine){
        Long id = newRoutine.getId();
        Routine oldRoutine = routineRepository.getById(id);

        oldRoutine.modifyRoutine(newRoutine.getSubtaskList());
        return routineRepository.save(oldRoutine);
    }

    @Transactional
    public void delete(Routine routine){
        routineRepository.delete(routine);
    }
}
