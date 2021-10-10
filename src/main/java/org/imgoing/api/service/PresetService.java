package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Preset;
import org.imgoing.api.repository.PresetRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresetService {
    private final PresetRepository presetRepository;

    @Transactional
    public Preset create(Preset newPreset){
        return presetRepository.save(newPreset);
    }

    @Transactional(readOnly = true)
    public Preset getById(Long id){
        return presetRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 업무입니다."));
    }

    @Transactional(readOnly = true)
    public List<Preset> getListByUserId(Long userId){
        return presetRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Preset> getListAll(){
        return presetRepository.findAll();
    }

    @Transactional
    public void delete(Preset preset){
        presetRepository.delete(preset);
    }

    @Transactional
    public Preset update(Preset newPreset){
        Long id = newPreset.getId();
        Preset oldPreset = getById(id);

        oldPreset.modifyPreset(newPreset);
        return presetRepository.save(oldPreset);
    }
}
