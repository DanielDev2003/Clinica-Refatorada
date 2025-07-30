public abstract class Leito {
    private TipoLeitoEnum tipoLeitoEnum;

    public Leito(TipoLeitoEnum tipoLeitoEnum){
        this.tipoLeitoEnum = tipoLeitoEnum;
    }

    public TipoLeitoEnum getTipoLeitoEnum(){
        return this.tipoLeitoEnum;
    }

    
    public abstract double calcularDiaria(int qtde_dias_internacao);
    

}
