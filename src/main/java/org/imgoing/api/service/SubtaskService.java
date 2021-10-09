package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Subtask;
import org.imgoing.api.repository.SubtaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;

    @Transactional
    public Subtask create(Subtask newSubtask){
        return subtaskRepository.save(newSubtask);
    }

    @Transactional(readOnly = true)
    public Subtask getById(Long id){
        return subtaskRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 세부업무입니다."));
    }

    @Transactional(readOnly = true)
    public List<Subtask> getListByUserId(Long userId){
        return subtaskRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Subtask> getListAll(){
        return subtaskRepository.findAll();
    }

    @Transactional
    public void delete(Subtask subtask){
        subtaskRepository.delete(subtask);
    }

    @Transactional
    public Subtask update(Subtask newSubtask){
        Long id = newSubtask.getId();
        Subtask oldSubtask = getById(id);

        oldSubtask.modifyTask(newSubtask);
        return subtaskRepository.save(oldSubtask);
    }
}
