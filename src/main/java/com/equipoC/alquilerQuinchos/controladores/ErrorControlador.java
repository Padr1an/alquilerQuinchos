package com.equipoC.alquilerQuinchos.controladores;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleErrors(HttpServletRequest httpRequest) {
        int httpErrorCode = getErrorCode(httpRequest);
        String errorMsg = "";

        switch (httpErrorCode) {
            case 400:
                errorMsg = "El recurso solicitado no existe.";
                break;
            case 403:
                errorMsg = "No tiene permisos para acceder al recurso.";
                break;
            case 401:
                errorMsg = "No se encuentra autorizado.";
                break;
            case 404:
                errorMsg = "El recurso solicitado no fue encontrado.";
                break;
            case 500:
                errorMsg = "Ocurrió un error interno.";
                break;
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("codigo", httpErrorCode);
        errorResponse.put("mensaje", errorMsg);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(httpErrorCode));
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
}
