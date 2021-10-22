package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.mapper.PlantaskMapper;
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
    public List<Plantask> saveAll(List<Plantask> plantasks) {
        return plantaskRepository.saveAll(plantasks);
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
                .orElseThrow(() -> new ImgoingException(ImgoingError.ROUTINE_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public List<Plantask> getListAll(){
        return plantaskRepository.findAll();
    }

    @Transactional
    public Plantask update(Plantask newPlantask){
        Long id = newPlantask.getId();
        Plantask oldPlantask = plantaskRepository.getById(id);

        // oldPlantask.modifyPlantask(newPlantask.getTaskList());
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
