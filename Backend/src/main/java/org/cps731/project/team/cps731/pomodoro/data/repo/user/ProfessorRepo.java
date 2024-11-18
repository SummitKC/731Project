package org.cps731.project.team.cps731.pomodoro.data.repo.user;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepo extends JpaRepository<Professor, Long> {

}
