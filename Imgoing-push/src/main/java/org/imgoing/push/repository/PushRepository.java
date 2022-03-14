package org.imgoing.push.repository;

import org.imgoing.push.document.Push;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRepository extends ReactiveCrudRepository<Push, String> {
}