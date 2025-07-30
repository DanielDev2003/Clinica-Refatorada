import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class ImpressaoConsole implements Impressao {
    private Prontuario prontuario;
    private Map<TipoProcedimentoEnum, Integer> procedimentos;

    public ImpressaoConsole (Prontuario prontuario){
        this.prontuario = prontuario;
        this.procedimentos = new HashMap<>();
        procedimentos.put(TipoProcedimentoEnum.BASICO,0);
        procedimentos.put(TipoProcedimentoEnum.COMUM,0);
        procedimentos.put(TipoProcedimentoEnum.AVANCADO,0);
    }


    @Override
    public String imprimaConta(){

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String conta = "----------------------------------------------------------------------------------------------"; 
        
        double valorDiarias = 0.0;
        if(prontuario.getInternacao() != null){
            valorDiarias += prontuario.calcularDiaria();
        }
        
        double precoTotalProcedimentos = 0.0;

		for (Procedimento p : prontuario.getProcedimentos()){
            precoTotalProcedimentos += p.getPrecoProcedimentos();
            TipoProcedimentoEnum tipoProcedimentoEnum = p.getTipoProcedimento();
            procedimentos.put(tipoProcedimentoEnum, procedimentos.get(tipoProcedimentoEnum)+1);
        }

        conta+= "\nA conta do(a) paciente " + prontuario.getNomePaciente() + " tem valor total de __ " + formatter.format(valorDiarias + precoTotalProcedimentos) + " __";
        conta += "\n\nConforme os detalhes abaixo:";

        if(prontuario.getInternacao()!= null){
            conta += "\n\nValor Total Diárias:\t\t\t" + formatter.format(valorDiarias);
 			conta += "\n\t\t\t\t\t" + prontuario.getQtdeDiasInternacao() + " diária" + (prontuario.getQtdeDiasInternacao() > 1 ? "s" : "")
 					+ " em " + (prontuario.getTipoLeitoEnum().equals(TipoLeitoEnum.APARTAMENTO) ? "apartamento" : "enfermaria");
        }

        if(prontuario.getProcedimentos().size() > 0){
            conta += "\n\nValor Total Procedimentos:\t\t" + formatter.format(precoTotalProcedimentos);

 			if (procedimentos.get(TipoProcedimentoEnum.BASICO) > 0) {
 				conta += "\n\t\t\t\t\t" + procedimentos.get(TipoProcedimentoEnum.BASICO) + " procedimento" + (procedimentos.get(TipoProcedimentoEnum.BASICO) > 1 ? "s" : "")
 						+ " básico" + (procedimentos.get(TipoProcedimentoEnum.BASICO) > 1 ? "s" : "");
            }

            if(procedimentos.get(TipoProcedimentoEnum.COMUM) > 0){
                conta += "\n\t\t\t\t\t" + procedimentos.get(TipoProcedimentoEnum.COMUM) + " procedimento" + (procedimentos.get(TipoProcedimentoEnum.COMUM) > 1 ? "s" : "")
 						+ " comu" + (procedimentos.get(TipoProcedimentoEnum.COMUM) > 1 ? "ns" : "m");
            }
            if(procedimentos.get(TipoProcedimentoEnum.AVANCADO) > 0){
                conta += "\n\t\t\t\t\t" + procedimentos.get(TipoProcedimentoEnum.AVANCADO) + " procedimento" + (procedimentos.get(TipoProcedimentoEnum.AVANCADO) > 1 ? "s" : "")
 						+ " avançado" + (procedimentos.get(TipoProcedimentoEnum.AVANCADO) > 1 ? "s" : "");
            }
        }    
    	conta += "\n\nVolte sempre, a casa é sua!";
 		conta += "\n----------------------------------------------------------------------------------------------";

 		return conta;
    }
}

