package tech.bgdigital.online.payment.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@Api(tags = "Admin utilisateur",description = ".")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

}
