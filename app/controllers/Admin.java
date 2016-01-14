package controllers;

import models.User;
import play.mvc.*;

@With(Secure.class)
public class Admin extends Controller {

	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullname);
        }
    }
	
    public static void index() {
        render();
    }

}
