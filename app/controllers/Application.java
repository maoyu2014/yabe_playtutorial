package controllers;

import play.*;
import play.data.validation.Required;
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
        render(post);
    }
    
    public static void postComment(Long postId,  @Required String author,  @Required String content) {
        Post post = Post.findById(postId);
        if (validation.hasErrors()) {
            render("Application/show.html", post);
        }
        post.addComment(author, content);
        /*
         * The current flash scope. The flash is a temporary storage mechanism 
         * that is a hash map You can store values associated with keys and later retrieve them.
         *  It has one special property: by default, values stored into the flash during 
         *  the processing of a request will be available during the processing of the 
         *  immediately following request. Once that second request has been processed, 
         *  those values are removed automatically from the storage This scope is very 
         *  useful to display messages after issuing a Redirect. 
         */
        flash.success("Thanks for posting %s", author);
        show(postId);	//这里巧妙的又调用了show方法
    }
    
}