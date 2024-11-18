package org.cps731.project.team.cps731.pomodoro.data.repo.user;

import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
