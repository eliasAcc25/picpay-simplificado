package PicPay.Simplificado.controller;

import PicPay.Simplificado.dto.TransferenciaRequest;
import PicPay.Simplificado.model.entity.Transferencia;
import PicPay.Simplificado.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferenciaRequest request) {
        try {
            Transferencia transferencia = transferenciaService.realizarTransferencia(
                    request.getValue(),
                    request.getPayer(),
                    request.getPayee()
            );
            return ResponseEntity.ok(transferencia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}
