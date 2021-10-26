package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.repository.RoutinetaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutinetaskService {
    private final TaskService taskService;
    private final RoutinetaskRepository routinetaskRepository;

    @Transactional
    public List<Routinetask> update(Routine routine, List<Long> updateTaskIdList) {
        List<Routinetask> routinetasks = routine.getRoutinetasks();

        List<Routinetask> removeList = routinetasks.stream()
                .filter(routinetask -> !updateTaskIdList.contains(routinetask.getTask().getId()))
                .collect(Collectors.toList());

        routinetasks.removeAll(removeList);
        this.deleteAll(removeList);

        List<Long> remainIdList = routinetasks.stream()
                .map(routinetask -> routinetask.getTask().getId())
                .collect(Collectors.toList());

        List<Routinetask> updateList = updateTaskIdList.stream()
                .filter(taskId -> !remainIdList.contains(taskId))
                .map(taskId -> Routinetask.builder()
                        .routine(routine)
                        .task(taskService.getById(taskId))
                        .build())
                .collect(Collectors.toList());

        updateList.addAll(routinetasks);
        routinetasks.clear();

        for(int i = 0; i < updateTaskIdList.size(); ++i) {
            for(Routinetask rt : updateList) {
                if(updateTaskIdList.get(i).equals(rt.getTask().getId())) {
                    rt.addPriority(i);
                    routinetasks.add(rt);
                    updateList.remove(rt);
                    break;
                }
            }
        }
        return this.createAll(routinetasks);
    }

    @Transactional
    public List<Routinetask> createAll(List<Routinetask> routinetasks){
        for(Routinetask rt : routinetasks) {
            if(!rt.getTask().getIsBookmarked())
                throw new ImgoingException(ImgoingError.BAD_REQUEST, "루틴에 북마크가 아닌 준비항목을 추가할 수 없습니다.");
        }
        return routinetaskRepository.saveAll(routinetasks);
    }

    @Transactional
    public void deleteAll(List<Routinetask> routinetasks){
        routinetaskRepository.deleteAll(routinetasks);
    }
}