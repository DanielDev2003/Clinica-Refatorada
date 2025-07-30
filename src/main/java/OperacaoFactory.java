import java.util.HashMap;
import java.util.Map;

public class OperacaoFactory {
    Map<String, Operacao> operacaoFactory;

    public OperacaoFactory(){
        this.operacaoFactory = new HashMap<>();
        operacaoFactory.put("basico", new Basica());
        operacaoFactory.put("comum", new Comum());
        operacaoFactory.put("avancado", new Avancado());
    }

    public Operacao criarInstanciaOperacao(String str){
        String operacao = str.toLowerCase();
        return operacaoFactory.get(operacao);
    }
}
