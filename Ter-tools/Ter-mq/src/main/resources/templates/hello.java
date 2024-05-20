package templates;


import com.cabin.ter.admin.mapper.PermissionDomainMapper;
import com.cabin.ter.admin.mapper.UserDomainMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "测试接口", description = "这是一个测试接口")
@RequestMapping("/test")
public class hello{
    @GetMapping("/hello")
    @Operation(summary = "测试接口")
    public String hello() {
        return "welcome";
    }
}
