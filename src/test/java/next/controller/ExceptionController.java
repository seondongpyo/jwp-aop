package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.ExceptionHandler;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import next.model.User;
import next.security.LoginUser;
import next.security.RequiredLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @RequestMapping("/exception-controller")
    public ModelAndView exception(@LoginUser User loginUser) {
        log.info("login user = {}", loginUser.getUserId());
        return new ModelAndView();
    }

    @ExceptionHandler(RequiredLoginException.class)
    public ModelAndView handleRequiredLoginException(HttpServletResponse response) {
        response.addHeader("exception", "handle RequiredLoginException in Controller");
        return new ModelAndView();
    }
}
