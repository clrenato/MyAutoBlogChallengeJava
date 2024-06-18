package challenge.repository;

import org.springframework.data.repository.CrudRepository;

import challenge.model.Post;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findAllByUserId(Long userId);
	
}
