package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.*;
import play.Play;
import play.data.validation.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	@Before
	public static void addDefaults() {
	    renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
	    renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
	}
	
    public static void index() {
    	Post frontPost = Post.find("order by postedAt desc").first();
    	List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
    	render(frontPost, olderPosts);
    }

    public static void show(Long id) {
        Post post = Post.findById(id);
        String randomID = Codec.UUID();
        render(randomID, post);
    }
    
    public static void postComment(
    		Long postId,  
    		@Required(message="Author is required") String author,  
    		@Required(message="A message is required") String content,
    		@Required(message="Please type the code") String code, 
    		String randomID)
    {
        Post post = Post.findById(postId);
        if(!Play.id.equals("test")) {
        	validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
        }
        if (validation.hasErrors()) {
            render("Application/show.html", post, randomID);
        }
        post.addComment(author, content);
        //flash可以在action之间传递一次值，在第二个action之后flash中的值将消失
        flash.success("Thanks for posting %s", author);
        Cache.delete(randomID);
        show(postId);	//这里巧妙的又调用了show方法
    }
    
    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        //d表示day，h表示小时，mi,mn,min表示分钟
        Cache.set(id, code, "10mn");	
        renderBinary(captcha);
    }
    
    public static void listTagged(String tag) {
        List<Post> posts = Post.findTaggedWith(tag);
        render(tag, posts);
    }
    
    public static void testJson() {
    	User user = new User("haczmy@qq.com", "123465", "maoyu");
    	renderJSON(user);
    }
    
    
    
}