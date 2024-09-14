package com.example.Nutriologa.Analia.Roman.service;

import org.springframework.stereotype.Service;

@Service
public class PagoService {

    public boolean procesarPago(String numeroTarjeta, String fechaExpiracion, String cvv, double monto) {
        // Lógica para procesar el pago
        // Este método debe integrar con un servicio de pago real
        return true; // Retorna true si el pago es exitoso
    }
}