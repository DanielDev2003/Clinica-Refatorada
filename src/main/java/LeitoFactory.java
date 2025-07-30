import java.util.HashMap;
import java.util.Map;

public class LeitoFactory{
    Map<String, Leito> leitoFactory;

    public LeitoFactory(){
        this.leitoFactory = new HashMap<>();
        leitoFactory.put("enfermaria", new Enfermaria());
        leitoFactory.put("apartamento", new Apartamento());
    }

    public Leito criarInstanciaLeito(String str){
        String nome = str.toLowerCase();
        return leitoFactory.get(nome);
    }
}