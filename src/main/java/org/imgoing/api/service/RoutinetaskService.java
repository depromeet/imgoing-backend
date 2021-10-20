package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.repository.RoutinetaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutinetaskService {
    private final RoutinetaskRepository routinetaskRepository;

    @Transactional
    public List<Routinetask> saveAll(List<Routinetask> routinetasks){
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
