package spring.boot.authenauthor.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import spring.boot.authenauthor.entities.Action;
import spring.boot.authenauthor.entities.Models;
import spring.boot.authenauthor.repositories.ModelsRepository;
import spring.boot.authenauthor.services.IModelService;

@Service
public class ModelsServiceImpl extends BaseServiceImpl<Models, Integer, ReactiveCrudRepository<Models,Integer>> implements IModelService {
//
//
    @Autowired
    private ModelsRepository modelsRepository;

    // Constructor truyền PostsRepository vào BaseServiceImpl
    public ModelsServiceImpl(ModelsRepository modelsRepository) {
        super(modelsRepository); // Truyền repository vào lớp cha
//        this.postsRepository = postsRepository; // Gán repository cho biến instance
    }




//    @Override
//    @PreAuthorize("@authorUtils.hasPermissionRaw(#id, #action)")
//    public Optional<Posts> getPostById(Integer id , String action) {
//        Optional<Posts> postsOptional = postsRepository.findById(id);
//        return postsOptional;
//    }

//    @Override
//    public List<Posts> getAllPosts() {
//        return postsRepository.findAll();
//    }
//
//    @Override
//    @PreAuthorize("@authorUtils.hasPermissionRaw(#id, #action)")
//    public Optional<Posts> getPostById(Integer id , String action) {
//        Optional<Posts> postsOptional = postsRepository.findById(id);
//        return postsOptional;
//    }
//
//    @Override
//    public Posts createPost(Posts posts) {
//        return postsRepository.save(posts);
//    }
//
//
//    @Override
//    public Posts updatePost(Integer id, Posts updatedPosts) {
//        return postsRepository.findById(id)
//            .map(post -> {
//                post.setTitle(updatedPosts.getTitle());
//                post.setBody(updatedPosts.getBody());
//                return postsRepository.save(post);
//            }).orElseThrow(() -> new RuntimeException("Post not found with id " + id));
//    }
//
//    @Override
//    public void deletePost(Integer id) {
//        postsRepository.deleteById(id);
//    }
//


}
