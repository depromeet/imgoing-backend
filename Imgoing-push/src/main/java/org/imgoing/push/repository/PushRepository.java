package org.imgoing.push.repository;

import org.imgoing.push.document.Push;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRepository extends ReactiveMongoRepository<Push, String> {
}