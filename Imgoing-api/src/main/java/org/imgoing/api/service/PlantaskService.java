package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.repository.PlantaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantaskService {
    private final PlantaskRepository plantaskRepository;

    @Transactional
    public void saveAll(Plan plan, List<Task> taskList) {
        List<Plantask> plantaskList = new ArrayList<>();
        for(int i = 0; i < taskList.size(); i++) {
            plantaskList.add(
                    Plantask.builder()
                    .plan(plan)
                    .task(taskList.get(i))
                    .sequence(i)
                    .build()
            );
        }
        plantaskRepository.saveAll(plantaskList);
    }

    @Transactional
    public Long deleteByPlanId(Long planId) {
        return plantaskRepository.deleteByPlanId(planId);
    }

    @Transactional
    public Plantask create(Plantask newPlantask){
        return plantaskRepository.save(newPlantask);
    }

    @Transactional(readOnly = true)
    public Plantask getById(Long id){
        return plantaskRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 루틴입니다."));
    }

    @Transactional(readOnly = true)
    public List<Task> getTaskListByPlan(Plan plan) {
        return plantaskRepository.getByPlanOrderBySequenceAsc(plan).stream()
                .map(plantask -> plantask.getTask())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Plantask> getListAll(){
        return plantaskRepository.findAll();
    }

    @Transactional
    public Plantask update(Plantask newPlantask){
        Long id = newPlantask.getId();
        Plantask oldPlantask = plantaskRepository.getById(id);
        return plantaskRepository.save(oldPlantask);
    }

    @Transactional
    public void delete(Plantask plantask){
        plantaskRepository.delete(plantask);
    }

    @Transactional
    public void deleteAll(List<Plantask> plantasks) {
        plantaskRepository.deleteAll(plantasks);
    }
}
