public class Operacao {

    private TipoProcedimentoEnum tipoProcedimentoEnum;
    private double preco;

    public Operacao(TipoProcedimentoEnum tipoProcedimentoEnum, double preco){
        this.tipoProcedimentoEnum = tipoProcedimentoEnum;
        this.preco = preco;
    }

    public TipoProcedimentoEnum getTipoProcedimentoEnum() {
        return tipoProcedimentoEnum;
    }

    public void setTipoProcedimentoEnum(TipoProcedimentoEnum tipoProcedimentoEnum) {
        this.tipoProcedimentoEnum = tipoProcedimentoEnum;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    

}
