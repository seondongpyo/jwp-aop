package core.mvc;

import core.mvc.tobe.HandlerExecution;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    private HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    private HandlerExecutor handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);

    private ExceptionHandlerMapping exceptionHandlerMapping;

    private ExceptionHandlerExecutor exceptionHandlerExecutor = new ExceptionHandlerExecutor();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMpping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void setExceptionHandlerMapping(ExceptionHandlerMapping exceptionHandlerMapping){
        this.exceptionHandlerMapping = exceptionHandlerMapping;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(req);
            if (!maybeHandler.isPresent()) {
                resp.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            ModelAndView mav = handlerExecutor.handle(req, resp, maybeHandler.get());
            render(mav, req, resp);
        } catch (Throwable e) {
            handlerException(req, resp, e);
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }

    private void handlerException(HttpServletRequest req, HttpServletResponse resp, Throwable e) throws ServletException {
        HandlerExecution handler = Optional.ofNullable(this.exceptionHandlerMapping)
            .map(exceptionHandlerMapping -> exceptionHandlerMapping.getHandler(e))
            .orElseThrow(() -> {
                logger.error("Exception : {}", e);
                return new ServletException(e.getMessage());
            });

        try {
            ModelAndView mav = exceptionHandlerExecutor.handle(req, resp, e, handler);
            render(mav, req, resp);
        } catch (Exception ex) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
