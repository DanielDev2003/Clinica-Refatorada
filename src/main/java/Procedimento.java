public class Procedimento {

	private Operacao operacao;

	public Procedimento(Operacao operacao) {
		this.operacao = operacao;
	}

	public TipoProcedimentoEnum getTipoProcedimento() {
		return this.operacao.getTipoProcedimentoEnum();
	}

	public double getPrecoProcedimentos(){
		return operacao.getPreco();
	}
}
