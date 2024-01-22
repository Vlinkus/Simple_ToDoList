package lt.github.vli.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.github.vli.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
