public class Enfermaria extends Leito {
    
    public Enfermaria(){
        super(TipoLeitoEnum.ENFERMARIA);
    }

    @Override
    public double calcularDiaria(int qtde_dias_internacao){
        if(qtde_dias_internacao <= 3){
            return 40.0 * qtde_dias_internacao;
        }else if(qtde_dias_internacao <=8){
            return 35.00 * qtde_dias_internacao;
        }else{
            return 30.0 * qtde_dias_internacao;
        }
    }
}
