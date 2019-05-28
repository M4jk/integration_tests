package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        List<LikePost> likePosts = new ArrayList<>();
        likePosts.add(likePost);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("entry");
        blogPost.setLikes(likePosts);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void shouldFindNoLikePosts() {
        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts.size(), is(0));
    }

    @Test
    public void shouldStoreANewLikePost() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        entityManager.persist(likePost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        entityManager.persist(likePost);

        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts.size(), is(1));
        Assert.assertThat(likePosts.contains(likePost), is(true));
    }

    @Test
    public void shouldGetCorrectLikePostData() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        entityManager.persist(likePost);

        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts.get(0).getPost(), is(blogPost));
        Assert.assertThat(likePosts.get(0).getUser(), is(user));
    }

    @Test
    public void shouldFindLikePostIfUserAndPostAreCorrect() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        entityManager.persist(likePost);

        Optional<LikePost> optionalLikePost = likePostRepository.findByUserAndPost(user, blogPost);
        Assert.assertThat(optionalLikePost.get(), is(likePost));
    }

    @Test
    public void shouldNotFindLikePostIfUserIsNotCorrect() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        entityManager.persist(likePost);

        User testUser = new User();
        testUser.setFirstName("test");
        testUser.setEmail("test@gmail.com");
        testUser.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(testUser);

        Optional<LikePost> optionalLikePost = likePostRepository.findByUserAndPost(testUser, blogPost);
        Assert.assertThat(optionalLikePost, is(Optional.empty()));
    }

    @Test
    public void shouldNotFindLikePostIfBlogPostIsNotCorrect() {
        userRepository.save(user);

        BlogPost blogPostTest = new BlogPost();
        blogPostTest.setEntry("text");
        blogPostTest.setUser(user);
        blogPostTest.setLikes(new ArrayList<>());
        blogPostRepository.save(blogPostTest);
        entityManager.persist(blogPostTest);

        Optional<LikePost> optionalLikePost = likePostRepository.findByUserAndPost(user, blogPostTest);
        Assert.assertThat(optionalLikePost, is(Optional.empty()));
    }

}
