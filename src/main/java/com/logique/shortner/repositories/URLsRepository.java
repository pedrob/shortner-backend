package  com.logique.shortner.repositories;

import com.logique.shortner.models.URLs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface URLsRepository extends JpaRepository<URLs, String> {
    
    @Override
    Optional<URLs> findById(String id);
}