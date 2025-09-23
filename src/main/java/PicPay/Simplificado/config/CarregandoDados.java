package PicPay.Simplificado.config;

import PicPay.Simplificado.model.entity.Saldo;
import PicPay.Simplificado.model.entity.User;
import PicPay.Simplificado.model.enums.TipoUsuario;
import PicPay.Simplificado.repository.SaldoRepository;
import PicPay.Simplificado.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test") // NÃO executa no perfil de teste
public class CarregandoDados implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Só carrega dados se o banco estiver vazio
        if (usuarioRepository.count() == 0) {
            carregarDadosIniciais();
        }
    }

    private void carregarDadosIniciais() {
        System.out.println("Carregando dados iniciais...");

        // Criar usuário comum 1
        User usuario1 = new User();
        usuario1.setNomeCompleto("João Silva");
        usuario1.setCpfOuCnpj("12345678901");
        usuario1.setEmail("joao@email.com");
        usuario1.setSenha("senha123");
        usuario1.setTipo(TipoUsuario.COMUM);
        usuarioRepository.save(usuario1);

        // Criar saldo para usuário 1
        Saldo saldo1 = new Saldo(usuario1, new BigDecimal("1000.00"));
        saldoRepository.save(saldo1);

        // Criar usuário comum 2
        User usuario2 = new User();
        usuario2.setNomeCompleto("Maria Santos");
        usuario2.setCpfOuCnpj("98765432109");
        usuario2.setEmail("maria@email.com");
        usuario2.setSenha("senha456");
        usuario2.setTipo(TipoUsuario.COMUM);
        usuarioRepository.save(usuario2);

        // Criar saldo para usuário 2
        Saldo saldo2 = new Saldo(usuario2, new BigDecimal("500.00"));
        saldoRepository.save(saldo2);

        // Criar lojista
        User lojista = new User();
        lojista.setNomeCompleto("Loja do João");
        lojista.setCpfOuCnpj("12345678000195");
        lojista.setEmail("loja@email.com");
        lojista.setSenha("senha789");
        lojista.setTipo(TipoUsuario.LOJISTA);
        usuarioRepository.save(lojista);

        // Criar saldo para lojista
        Saldo saldoLojista = new Saldo(lojista, new BigDecimal("0.00"));
        saldoRepository.save(saldoLojista);

        System.out.println("Dados carregados:");
        System.out.println("- João Silva (ID: " + usuario1.getId() + ") - Saldo: R$ 1000,00");
        System.out.println("- Maria Santos (ID: " + usuario2.getId() + ") - Saldo: R$ 500,00");
        System.out.println("- Loja do João (ID: " + lojista.getId() + ") - Saldo: R$ 0,00");
    }
}
