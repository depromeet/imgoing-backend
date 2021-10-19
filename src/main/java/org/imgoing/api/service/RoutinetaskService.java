package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.repository.RoutinetaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutinetaskService {
    private final RoutinetaskRepository routinetaskRepository;

    @Transactional
    public List<Routinetask> saveAll(List<Routinetask> routinetasks){
        return routinetaskRepository.saveAll(routinetasks);
    }

    @Transactional
    public void deleteAllById(List<Long> removeIdList){
        routinetaskRepository.deleteAllById(removeIdList);
    }
}
