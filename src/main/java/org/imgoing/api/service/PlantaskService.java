package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.repository.PlantaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantaskService {
    private final PlantaskRepository plantaskRepository;

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

        oldPlantask.modifyRoutine(newPlantask.getSubtaskList());
        return plantaskRepository.save(oldPlantask);
    }

    @Transactional
    public void delete(Plantask plantask){
        plantaskRepository.delete(plantask);
    }
}
