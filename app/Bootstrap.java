import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;
import models.User;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {

		if(User.count() == 0) {
            Fixtures.loadModels("initial-data.yml");
        }
        
    }
	
}
