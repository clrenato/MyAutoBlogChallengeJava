package challenge.controller;

import challenge.model.Post;
import challenge.model.User;
import challenge.security.AuthenticationFacade;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import challenge.repository.PostRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

	@Autowired
	private PostRepository repository;

	@Autowired
	private AuthenticationFacade authentication;

	@GetMapping("/posts")
	public List<Post> listPosts() {
		User user = authentication.getUser();

		return Lists.newArrayList(repository.findAllByUserId(user.getId()));
	}

	@GetMapping("/posts/{id}")
	public Post getPostById(@PathVariable Long id) {
		Optional<Post> opt = repository.findById(id);

		if (opt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST ID " + id + "not found");
		}

		return opt.get();
	}

	@PostMapping("/posts")
	public Post newPost(@Valid @RequestBody Post post) {
		post.setUser(authentication.getUser());

		post.setDate(new Date());
		return repository.save(post);
	}

	@PutMapping("/posts/{id}")
	public Post updatePost(@PathVariable Long id, @Valid @RequestBody Post postParam) {

		Optional<Post> opt = repository.findById(id);

		if (opt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST ID " + id + "not found");
		}

		Post post = opt.get();
		post.setTitle(postParam.getTitle());
		post.setText(postParam.getText());

		repository.save(post);

		return post;
	}


	@DeleteMapping("/posts/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id) {
		repository.deleteById(id);
		return new ResponseEntity<>("DELETED successfully", HttpStatus.OK);
	}

}
