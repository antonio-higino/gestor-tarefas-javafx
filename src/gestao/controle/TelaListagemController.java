package gestao.controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import gestao.dao.TarefaDAO;
import gestao.modelo.Tarefa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TelaListagemController {
	
	ObservableList<String> responsavelList = FXCollections.observableArrayList("Select","Ana","Antonio","Carlos","João");
	ObservableList<String> situacaoList = FXCollections.observableArrayList("Em andamento","Concluida");
	
	private Stage listagemStage;
	
	@FXML
    private TextField textFieldTitulo;

    @FXML
    private ChoiceBox<String> choiceBoxResponsavel;

    @FXML
    private TextArea textAreaListagem;

    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private TextField textFieldNumero;

    @FXML
    private ChoiceBox<String> choiceBoxSituacao;

    @FXML
    private Button botaoBuscar;
    
    @FXML
    private Button botaoConcluir;

    @FXML
    private Button botaoExcluirTarefa;

    @FXML
    private Button botaoEditarTarefa;
    
    @FXML
    private void initialize() {
    	
    	textFieldTitulo.setText(null);
    	textAreaDescricao.setText(null);
    	textFieldNumero.setText(null);
    	
    	choiceBoxSituacao.setValue("Em andamento");
    	choiceBoxSituacao.setItems(situacaoList);
    	
    	choiceBoxResponsavel.setValue("Select");
    	choiceBoxResponsavel.setItems(responsavelList);
    	
    	listagemSemFiltros(false);
    }
    
    @FXML
    void concluirTarefa(ActionEvent event) throws IOException {
    	try {
    		TarefaDAO banco = TarefaDAO.getInstance();
        	
        	int id = Integer.parseInt(textFieldNumero.getText());
        	
        	if(banco.tarefaExiste(id)) {
    			banco.getTarefaEspecifica(id).setConcluida(true);
    			textAreaListagem.setText("\nTarefa #" + id + " foi concluida\n");
    		} else {
    			textAreaListagem.setText("\nTarefa #" + id + " não existe\n");
    		}	
    	} catch(NumberFormatException ex) {
    		textAreaListagem.setText("Número inválido");
    		
    		textFieldTitulo.setText(null);
        	textAreaDescricao.setText(null);
        	textFieldNumero.setText(null);
        	choiceBoxResponsavel.setValue("Select");
    	}
    }

    @FXML
    void excluirTarefa(ActionEvent event) throws IOException {
    	try {
    		TarefaDAO banco = TarefaDAO.getInstance();
        	
        	int id = Integer.parseInt(textFieldNumero.getText());
        	
        	if(banco.tarefaExiste(id)) {
        		banco.getTarefas().remove(banco.getTarefaEspecifica(id));
    			textAreaListagem.setText("\nTarefa #" + id + " foi excluida\n");
    		} else {
    			textAreaListagem.setText("\nTarefa #" + id + " não existe\n");
    		}	
    	} catch(NumberFormatException ex) {
    		textAreaListagem.setText("Número inválido");
    		
    		textFieldTitulo.setText(null);
        	textAreaDescricao.setText(null);
        	textFieldNumero.setText(null);
        	choiceBoxResponsavel.setValue("Select");
    	}
    }
    
    private boolean existemDadosEntrada() {
    	boolean resultado = false;
    	
    	if(textFieldNumero.getText() != null) {
    		resultado = true;
    	} else if(textFieldTitulo.getText() != null) {
    		resultado = true;
    	} else if(textAreaDescricao.getText() != null) {
    		resultado = true;
    	} else if(choiceBoxResponsavel.getValue() != "Select") {
    		resultado = true;
    	}
    	
    	return resultado;
    }
    
    private void listagemSemFiltros(boolean concluida) {
    	TarefaDAO banco = TarefaDAO.getInstance();
		
		ArrayList<Integer> resultado = new ArrayList<Integer>();
		
		for(Tarefa tarefa : banco.getTarefas()) {
			if(concluida == tarefa.isConcluida()) {
				resultado.add(tarefa.getId());
			}
		}
		textAreaListagem.setText(banco.listarTarefas(resultado, concluida));
    }

    @FXML
    void buscarResultados(ActionEvent event) throws IOException {
    	try {
	    	TarefaDAO banco = TarefaDAO.getInstance();
			
			boolean concluida = false;
			
			if(choiceBoxSituacao.getValue().equals("Em andamento")) {
				concluida = false;
			} else if (choiceBoxSituacao.getValue().equals("Concluida")) {
				concluida = true;
			}
			
			//Busca sem filtros
			if(!existemDadosEntrada()) {
				listagemSemFiltros(concluida);
			} else {
				//Busca com filtros
				
				Set<Integer> resultadoNumero = new HashSet<Integer>();
				Set<Integer> resultadoTitulo = new HashSet<Integer>();
				Set<Integer> resultadoDescricao = new HashSet<Integer>();
				Set<Integer> resultadoResponsavel = new HashSet<Integer>();
				
				String titulo = new String(textFieldTitulo.getText() + "");
				String descricao = new String(textAreaDescricao.getText() + "");
				
				for(Tarefa tarefa : banco.getTarefas()) {
					if(concluida == tarefa.isConcluida()) {
						if(titulo.equals(tarefa.getTitulo())) {
							resultadoTitulo.add(tarefa.getId());
						}
						
						if(descricao.equals(tarefa.getDescricao())) {
							resultadoDescricao.add(tarefa.getId());
						}
						
						if(choiceBoxResponsavel.getValue() == tarefa.getResponsavel()) {
							resultadoResponsavel.add(tarefa.getId());
						}
						
						if(textFieldNumero.getText() != null) {
							int id = Integer.parseInt(textFieldNumero.getText());
							if(id == tarefa.getId()) {
								resultadoNumero.add(tarefa.getId());
							}
						}
					}
				}
				
				Set<Integer> resultadoDesordenado = new HashSet<Integer>();
				
				for(Tarefa tarefa : banco.getTarefas()) {
					resultadoDesordenado.add(tarefa.getId());
				}
				
				//Checa por intersecoes para filtrar usando mais de um filtro ao mesmo tempo
				if(resultadoNumero.size() > 0) {
					resultadoDesordenado.retainAll(resultadoNumero);
				}
				if(resultadoTitulo.size() > 0) {
					resultadoDesordenado.retainAll(resultadoTitulo);
				}
				if(resultadoDescricao.size() > 0) {
					resultadoDesordenado.retainAll(resultadoDescricao);
				}
				if(resultadoResponsavel.size() > 0) {
					resultadoDesordenado.retainAll(resultadoResponsavel);
				}
				
				//Ordena o resultado da intersecao
				TreeSet resultadoOrdenado = new TreeSet(resultadoDesordenado);
				
				ArrayList<Integer> resultadoFinal = new ArrayList<Integer>();
				
				resultadoFinal.addAll(resultadoOrdenado);
				
				textAreaListagem.setText(banco.listarTarefas(resultadoFinal, concluida));
				
				textFieldTitulo.setText(null);
		    	textAreaDescricao.setText(null);
		    	textFieldNumero.setText(null);
		    	choiceBoxResponsavel.setValue("Select");
			}
    	} catch(NumberFormatException ex) {
    		textAreaListagem.setText("Número inválido");
    		
    		textFieldTitulo.setText(null);
        	textAreaDescricao.setText(null);
        	textFieldNumero.setText(null);
        	choiceBoxResponsavel.setValue("Select");
    	}
    }
    
    public void setListagemStage(Stage listagemStage) {
		this.listagemStage = listagemStage;
	}
    
    @FXML
    void abrirTelaEdicao(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(TelaEdicaoController.class.getResource("/gestao/visao/TelaEdicao.fxml"));
    	AnchorPane page = (AnchorPane) loader.load();
    	
    	Stage edicaoStage = new Stage();
    	edicaoStage.setTitle("Edição de Tarefas");
    	edicaoStage.setResizable(false);
    	Scene scene = new Scene(page);
    	edicaoStage.setScene(scene);
    	
    	TelaEdicaoController controller = loader.getController();
    	controller.setEdicaoStage(edicaoStage);
    	edicaoStage.showAndWait();
    }
}
