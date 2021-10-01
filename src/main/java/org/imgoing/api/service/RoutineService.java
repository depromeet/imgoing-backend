package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.entity.Subtask;
import org.imgoing.api.entity.Task;
import org.imgoing.api.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;

    @Transactional
    public Routine create(Routine newRoutine){
        return routineRepository.save(newRoutine);
    }
}
