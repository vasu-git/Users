package test.service.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@SpringBootApplication
public class UsersController extends SpringBootServletInitializer{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${topic}")
	private String topic;
	
    private static final Logger logger = LogManager.getLogger(UsersController.class);

    @CachePut(value = "user", key = "#name")
	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	public @ResponseBody User addNewUser(@RequestParam String name, @RequestParam String email)
	{
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		userRepository.save(user);
		logger.info("Adding user "+name);
		this.sendMessage("POST: Added user "+name);
		return user;
	}
	
	
    @Cacheable(value = "user", key = "#id")
	@RequestMapping("/users/{id}")
	public @ResponseBody User getUserById(@PathVariable String id)
	{
		this.sendMessage("GET: User "+id);
		logger.info("Getting user : "+id);
		return userRepository.findUserById(Integer.parseInt(id));
	}
    
    
    @Cacheable(value = "user", key = "#name")
    @RequestMapping("/users/name/{name}")
    public @ResponseBody User getUserByName(@PathVariable String name)
    {
    	this.sendMessage("GET: User By name "+name);
    	logger.info("Getting user by name : "+name);
    	return userRepository.findUserByName(name);
    }
    
	@RequestMapping("/users")
	public @ResponseBody Iterable<User> users()
	{
		this.sendMessage("GET: All users");
		logger.info("Getting all users");
		return userRepository.findAll();
	}
	
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	{
	    return application.sources(UsersController.class);
	}
	
	private void sendMessage(String data)
	{
		kafkaTemplate.send(topic, data);
	}
}
