package org.imgoing.api.service;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.repository.TaskRepository;
import org.imgoing.api.support.ImgoingError;
import org.imgoing.api.support.ImgoingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public Task create(Task newTask){
        return taskRepository.save(newTask);
    }

    @Transactional(readOnly = true)
    public Task getById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new ImgoingException(ImgoingError.BAD_REQUEST, "존재하지 않는 준비항목입니다."));
    }

    @Transactional(readOnly = true)
    public List<Task> getListById(List<Long> idList){
        return idList.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Task> getListByUserId(Long userId){
        return taskRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Task> getListAll(){
        return taskRepository.findAll();
    }

    @Transactional
    public void delete(Task task){
        taskRepository.delete(task);
    }

    @Transactional
    public Task update(Task newTask){
        Long id = newTask.getId();
        Task oldTask = getById(id);

        oldTask.modifyTask(newTask);
        return taskRepository.save(oldTask);
    }

    @Transactional
    public List<Task> saveAll(List<Task> tasks) {
        return taskRepository.saveAll(tasks);
    }

    @Transactional
    public void deleteAll(List<Task> tasks) {
        taskRepository.deleteAll(tasks);
    }
}
