package com.example.Nutriologa.Analia.Roman.controller;

import com.example.Nutriologa.Analia.Roman.model.PagoRequest;
import com.example.Nutriologa.Analia.Roman.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@RequestBody PagoRequest pagoRequest) {
        boolean pagoExitoso = pagoService.procesarPago(
                pagoRequest.getNumeroTarjeta(),
                pagoRequest.getFechaExpiracion(),
                pagoRequest.getCvv(),
                pagoRequest.getMonto()
        );

        if (pagoExitoso) {
            return ResponseEntity.ok("Pago procesado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar el pago");
        }
    }
}