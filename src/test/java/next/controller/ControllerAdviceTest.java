package next.controller;

import core.di.context.support.AnnotationConfigApplicationContext;
import core.mvc.DispatcherServlet;
import core.mvc.tobe.*;
import next.config.MyConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerAdviceTest {

    private DispatcherServlet dispatcher;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(ac, ac.getBean(HandlerConverter.class));
        dispatcher = new DispatcherServlet();
        dispatcher.addHandlerMapping(ahm);
        dispatcher.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        ExceptionHandlerConverter exceptionHandlerConverter = ac.getBean(ExceptionHandlerConverter.class);
        dispatcher.addExceptionHandlerMapping(new ControllerExceptionHandlerMapping(ac, exceptionHandlerConverter));
        dispatcher.addExceptionHandlerMapping(new ControllerAdviceExceptionHandlerMapping(ac, exceptionHandlerConverter));
    }

    @DisplayName("RequiredLoginException 예외가 발생하면 로그인 페이지로 이동한다.")
    @Test
    void loginRequired() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/questions");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcher.service(request, response);

        assertThat(response.getRedirectedUrl()).isEqualTo("/users/loginForm");
    }
    
    @DisplayName("@Controller 내부의 @ExceptionHandler 로 예외 처리")
    @Test
    void handleExceptionByController() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/exception-controller");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcher.service(request, response);

        assertThat(response.getHeader("exception")).isEqualTo("handle IllegalArgumentException");
    }
}
