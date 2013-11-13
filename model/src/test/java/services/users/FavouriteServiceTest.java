package services.users;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.Role;
import model.User;
import model.dao.FavouriteDAO;
import model.dao.ObstacleDAO;
import model.dao.RoleDAO;
import model.dao.UserDAO;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class FavouriteServiceTest {

	private FavouriteService favouriteService;
	private UserDAO userDAO;
	private RoleDAO roleDAO;
	private FavouriteDAO favouriteDAO;

	@Before
	public void configure() throws NamingException {
		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.core.LocalInitialContextFactory");

		p.put("openejb.deployments.classpath.ear", "true");

		p.put("lazarus-persistence-unit", "new://Resource?type=DataSource");
		p.put("lazarus-persistence-unit.JdbcDriver", "org.postgresql.Driver");
		p.put("lazarus-persistence-unit.JdbcUrl",
				"jdbc:postgresql://localhost/lazarus");
		p.put("lazarus-persistence-unit.JdbcUrl",
				"jdbc:postgresql://localhost/lazarus");
		p.put("lazarus-persistence-unit.Username", "postgres");
		p.put("lazarus-persistence-unit.Password", "postgres");

		Context context = new InitialContext(p);

		favouriteService = (FavouriteService) context
				.lookup("FavouriteServiceLocal");
		userDAO = (UserDAO) context.lookup("UserDAOLocal");
		roleDAO = (RoleDAO) context.lookup("RoleDAOLocal");
		favouriteDAO = (FavouriteDAO) context.lookup("FavouriteDAOLocal");

		// Create Role
		Role role = roleDAO.find("user");
		if (role == null) {
			role = new Role();
			role.setName("user");
			roleDAO.add(role);
			role = roleDAO.find("user");
		}

		// Create User
		User user = userDAO.find("mateo");
		if (user == null) {
			user = new User();
			user.setActive(true);
			user.setCellphone("099584756");
			user.setEmail("mdr@gmail.com");
			user.setPassword("111");
			user.setRole(role);
			user.setSecretAnswer("mateo");
			user.setSecretQuestion("como me llamo?");
			user.setUsername("mateo");
			userDAO.add(user);
		}

	}

	@Test
	public void testAddToFavourite() {
		Coordinate position = new Coordinate(-34.902651, -56.162756);
		GeometryFactory factory = new GeometryFactory();
		Point point = factory.createPoint(position);
		User user = userDAO.find("mateo");
		String name = "casa";
		if (favouriteDAO.findByUserAndName(user, name) != null) {
			favouriteService.removeFromFavourite(user, name);
		}
		//favouriteService.addToFavourite(user, point, name);
		// boolean added = false;
		// Obstacle obstacle = obstacleDAO.find(point);
		// if (obstacle != null && point.equals(obstacle.getCentre())
		// && obstacle.getRadius() == 2) {
		// User possibleUser = obstacle.getUser();
		// if (possibleUser != null && possibleUser.equals(user)) {
		// added = true;
		// }
		// }
		// /*
		// boolean deleted = false;
		// obstacleService.deactivateObstacle(point);
		// if (obstacleDAO.find(point) == null) {
		// deleted = true;
		// }
		// assert (added && deleted);
		// */
		//
		// }

	}

}
