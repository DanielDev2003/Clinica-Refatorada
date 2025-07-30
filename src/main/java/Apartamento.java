public class Apartamento extends Leito{
    
    public Apartamento(){
        super(TipoLeitoEnum.APARTAMENTO);
    }

    @Override
    public double calcularDiaria(int qtde_dias_internacao){
        if(qtde_dias_internacao <= 3){
            return 100.00 * qtde_dias_internacao;
        }else if(qtde_dias_internacao <= 8){
            return 90.00 * qtde_dias_internacao;
        }else{
            return 80.00 * qtde_dias_internacao;
        }
    }

} 
