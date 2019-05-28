package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    private User confirmedUser;
    private User blogPostUser;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        confirmedUser = new User();
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);
        confirmedUser.setEmail("confirmed@gmail.com");
        confirmedUser.setFirstName("Confir");
        confirmedUser.setLastName("Med");
        confirmedUser.setId(100L);

        blogPostUser = new User();
        blogPostUser.setFirstName("Jan");
        blogPostUser.setLastName("Kowalski");
        blogPostUser.setEmail("john@domain.com");
        blogPostUser.setAccountStatus(AccountStatus.NEW);
        blogPostUser.setId(101L);

        blogPost = new BlogPost();
        blogPost.setUser(blogPostUser);
        blogPost.setEntry("entry");
        blogPost.setId(10L);
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void onlyUserWithConfirmedStatusShouldBeAbleToLikePost() {
        when(userRepository.findById(confirmedUser.getId())).thenReturn(Optional.ofNullable(confirmedUser));
        when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.ofNullable(blogPost));
        when(likePostRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(Optional.empty());

        blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId());

        ArgumentCaptor<LikePost> likePostParam = ArgumentCaptor.forClass(LikePost.class);
        verify(likePostRepository).save(likePostParam.capture());
        LikePost likePost = likePostParam.getValue();
        Assert.assertThat(likePost.getUser(), is(confirmedUser));
        Assert.assertThat(likePost.getPost(), is(blogPost));
    }

    @Test(expected = DomainError.class)
    public void shouldThrowDomainErrorIfUserWithoutConfirmedStatusTriesToLikePost() {
        when(userRepository.findById(blogPostUser.getId())).thenReturn(Optional.ofNullable(blogPostUser));
        when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.ofNullable(blogPost));
        when(likePostRepository.findByUserAndPost(blogPostUser, blogPost)).thenReturn(Optional.empty());

        blogService.addLikeToPost(blogPostUser.getId(), blogPost.getId());
    }
}
