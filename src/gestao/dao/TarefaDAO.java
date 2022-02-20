package gestao.dao;

import java.util.ArrayList;

import gestao.modelo.Tarefa;

public class TarefaDAO {
	
	private ArrayList<Tarefa> tarefas;
	private int contadorParaId = 0;
	
	private static TarefaDAO banco;
	
	public TarefaDAO() {
		tarefas = new ArrayList<Tarefa>();
	}
	
	public static TarefaDAO getInstance() {
		if(banco == null) {
			banco = new TarefaDAO();
		}
		return banco;
	}

	public ArrayList<Tarefa> getTarefas() {
		return tarefas;
	}
	
	public Tarefa getTarefaEspecifica(int id) {
		for(Tarefa tarefa : tarefas) {
			if(tarefa.getId() == id) {
				return tarefa;
			}
		}
		return null;
	}

	public int getContadorParaId() {
		contadorParaId++;
		return contadorParaId;
	}
	
	public void adicionarTarefa(Tarefa tarefa) {
		TarefaDAO banco = TarefaDAO.getInstance();
		
		banco.getTarefas().add(tarefa);
	}
	
	public boolean tarefaExiste(int id) {
		TarefaDAO banco = TarefaDAO.getInstance();
		
		for(Tarefa tarefa : banco.getTarefas()) {
			if(tarefa.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checarRepetido(int id, ArrayList<Integer> resultado) {
		for(int num : resultado) {
			if(num == id) {
				return true;
			}
		}
		return false;
	}
	
	public String listarTarefas(ArrayList<Integer> resultado, boolean concluida) {
		TarefaDAO banco = TarefaDAO.getInstance();
		
		String output = "";
		
		output += "\n--> Lista de Tarefas";
		
		if(concluida) {
			output += " - Concluidas:\n";
		} else {
			output += " - Em Andamento:\n";
		}
		
		for(int id : resultado) {
			output += "\n";
			output += "Tarefa #" + id + "\n";
			output += "Titulo: " + banco.getTarefaEspecifica(id).getTitulo() + "\n";
			output += "Descricao: " + banco.getTarefaEspecifica(id).getDescricao() + "\n";
			output += "Responsavel: " + banco.getTarefaEspecifica(id).getResponsavel() + "\n";
			output += "Prioridade: " + banco.getTarefaEspecifica(id).getPrioridade() + "\n";
			output += "Deadline: " + banco.getTarefaEspecifica(id).getDeadline()+ "\n";
		}
		
		if(resultado.size() == 0) {
			output += "\nNenhum resultado encontrado\n";
		}
		
		return output;
	}
}
