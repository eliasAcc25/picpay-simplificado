package PicPay.Simplificado.service;

import PicPay.Simplificado.model.entity.Saldo;
import PicPay.Simplificado.model.entity.Transferencia;
import PicPay.Simplificado.model.entity.User;
import PicPay.Simplificado.model.enums.StatusTransferencia;
import PicPay.Simplificado.repository.SaldoRepository;
import PicPay.Simplificado.repository.TransferenciaRepository;
import PicPay.Simplificado.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferenciaService {

    // AUTOWIRED SERVE PARA INJETAR DEPENDÊNCIAS, PARA SEREM USADAS SEM PRECISAR CRIAR MANUALMENTE
    @Autowired
    private SaldoRepository saldoRepository;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Transferencia realizarTransferencia(BigDecimal valor, Long pagadorId, Long recebedorId) {
        // 1. Buscar pagador e recebedor
        User pagador = usuarioRepository.findById(pagadorId)
                .orElseThrow(() -> new IllegalArgumentException("Pagador não encontrado"));
        User recebedor = usuarioRepository.findById(recebedorId)
                .orElseThrow(() -> new IllegalArgumentException("Recebedor não encontrado"));

        // 2. Verificar se pagador é lojista (não pode transferir)
        if (pagador.getTipo().name().equals("LOJISTA")) {
            throw new IllegalArgumentException("Lojista não pode realizar transferências.");
        }

        // 3. Verificar se pagador e recebedor são iguais
        if (pagador.getId().equals(recebedor.getId())) {
            throw new IllegalArgumentException("Pagador e recebedor devem ser diferentes.");
        }

        // 4. Buscar saldo do pagador
        Saldo saldoPagador = saldoRepository.findByUser_Id(pagadorId)
                .orElseThrow(() -> new IllegalArgumentException("Saldo do pagador não encontrado."));
        if (!saldoPagador.temSaldoSuficiente(valor)) {
            throw new IllegalArgumentException("Saldo insuficiente para transferência.");
        }

        // 5. Buscar saldo do recebedor
        Saldo saldoRecebedor = saldoRepository.findByUser_Id(recebedorId)
                .orElseThrow(() -> new IllegalArgumentException("Saldo do recebedor não encontrado."));

        // 6. Simular autorização externa
        if (!autorizarTransferencia()) {
            throw new IllegalArgumentException("Transferência não autorizada pelo serviço externo.");
        }

        // 7. Realizar a transação (débito e crédito)
        saldoPagador.debitar(valor);
        saldoRecebedor.creditar(valor);

        saldoRepository.save(saldoPagador);
        saldoRepository.save(saldoRecebedor);

        // 8. Criar e salvar a transferência
        Transferencia transferencia = new Transferencia(valor, pagador, recebedor);
        transferencia.setStatus(StatusTransferencia.AUTORIZADA);
        transferenciaRepository.save(transferencia);

        // 9. Simular notificação
        notificarRecebedor(recebedor, transferencia);

        return transferencia;
    }

    /**
     * Simula chamada ao serviço autorizador externo (mock).
     */
    private boolean autorizarTransferencia() {
        // Aqui você pode fazer uma chamada HTTP real, mas para fins didáticos vamos simular
        // Suponha que o serviço retorna "true" para autorizado, "false" para não autorizado
        return true; // Simulação: sempre autoriza
    }

    /**
     * Simula notificação ao recebedor (mock).
     */
    private void notificarRecebedor(User recebedor, Transferencia transferencia) {
        // Aqui você pode fazer uma chamada HTTP POST real para o serviço de notificação
        System.out.println("Notificando recebedor " + recebedor.getEmail() + " sobre a transferência " + transferencia.getId());
        // Simulação: apenas imprime no console
    }
}
