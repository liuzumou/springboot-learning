# Validation in Spring Boot
在springboot中使用参数效验

示例：
- post: localhost:8080/users 采用@valid web层效验
- post：localhost:8080/users 采用validator.validate在服务层手工效验

## 引入效验框架
spring-boot-starter-validation 是基于Hibernate Validator实现。

```xml
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-validation</artifactId> 
</dependency>
```

## 编写验证规则
```java
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotBlank(message = "Email is mandatory")
    private String email;
    
    // standard constructors / setters / getters / toString
        
}
```

## 编写效验异常处理
```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(MethodArgumentNotValidException.class)
public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });
    return errors;
}
```

## 验证参数
```java
@RestController
public class UserController {

    @PostMapping("/users")
    ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        // persisting the user
        return ResponseEntity.ok("User is valid");
    }
    
    // standard constructors / other methods
    
}

```
When Spring Boot finds an argument annotated with @Valid, it automatically bootstraps the default JSR 380 implementation — Hibernate Validator — and validates the argument.

效验规则查看：
https://beanvalidation.org/2.0/spec/

## 效验规则测试
编写测试类或者用postman测试
```java
@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    private MockMvc mockMvc;

    //...

}
```

## 参考
Validation in Spring Boot
https://www.baeldung.com/spring-boot-bean-validation

Spring Validation in the Service Layer
https://www.baeldung.com/spring-service-layer-validation