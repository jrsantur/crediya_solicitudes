package pe.crediya.solicitud.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.crediya.solicitud.api.config.AuthHandlerFilter;
import pe.crediya.solicitud.api.dto.ErrorResponseDto;
import pe.crediya.solicitud.api.dto.SolicitudRequest;
import pe.crediya.solicitud.api.dto.SolicitudResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final Handler solicitudHandler;
    private final AuthHandlerFilter authFilter;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitudes",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "registrarSolicitud",
                            summary = "Registrar solicitud de préstamo",
                            description = "Crea una nueva solicitud de préstamo con validaciones de negocio",
                            tags = {"Solicitudes de Préstamo"},
                            requestBody = @RequestBody(
                                    description = "Datos de la solicitud de préstamo",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = SolicitudRequest.class)
                                    )
                            ),
                            responses = {

                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud creada exitosamente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SolicitudResponse.class)
                                            )
                                    ),

                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Ya existe una solicitud activa para el cliente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),


    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route()
                .path("/api/v1/solicitudes", builder -> builder
                        .POST("", authFilter.requireAuth("CLIENTE", solicitudHandler::crearSolicitud))
                        .GET( "", authFilter.requireAuth("ASESOR", solicitudHandler::obtenerSolicitudPorRevision)))
                .build();

    }


}
