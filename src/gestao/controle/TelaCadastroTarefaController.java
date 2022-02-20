package gestao.controle;

import java.io.IOException;

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

public class TelaCadastroTarefaController {
	
	ObservableList<String> responsavelList = FXCollections.observableArrayList("Select","Ana","Antonio","Carlos","João");
	ObservableList<String> prioridadeList = FXCollections.observableArrayList("Select","Alta","Média","Baixa");

    @FXML
    private TextField textFieldTitulo;

    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private ChoiceBox<String> choiceBoxPrioridade;

    @FXML
    private ChoiceBox<String> choiceBoxResponsavel;

    @FXML
    private TextField textFieldDeadline;

    @FXML
    private Button botaoCadastrar;
    
    @FXML
    private TextArea textAreaAvisos;
    
    @FXML
    private Button botaoListar;
    
    @FXML
    private void initialize() {
    	
    	textFieldTitulo.setText(null);
    	textAreaDescricao.setText(null);
    	textFieldDeadline.setText(null);
    	
    	choiceBoxPrioridade.setValue("Select");
    	choiceBoxPrioridade.setItems(prioridadeList);
    	
    	choiceBoxResponsavel.setValue("Select");
    	choiceBoxResponsavel.setItems(responsavelList);
    }
    
    private boolean validarDadosEntrada() {
    	boolean resultado = true;
    	
    	if(textFieldTitulo.getText() == null) {
    		resultado = false;
    	} else if(textAreaDescricao.getText() == null) {
    		resultado = false;
    	} else if(choiceBoxResponsavel.getValue() == "Select") {
    		resultado = false;
    	} else if(choiceBoxPrioridade.getValue() == "Select") {
    		resultado = false;
    	} else if(textFieldDeadline.getText() == null) {
    		resultado = false;
    	}
    	
    	return resultado;
    }
    
    @FXML
    void cadastrarTarefa(ActionEvent event) {
    	if(validarDadosEntrada()) {
    		TarefaDAO banco = TarefaDAO.getInstance();
    		
    		int id = banco.getContadorParaId();
    		
    		Tarefa tarefa = new Tarefa();
    		
    		tarefa.setId(id);
    		tarefa.setTitulo(textFieldTitulo.getText());
    		tarefa.setDescricao(textAreaDescricao.getText());
    		tarefa.setResponsavel(choiceBoxResponsavel.getValue());
    		tarefa.setPrioridade(choiceBoxPrioridade.getValue());
    		tarefa.setDeadline(textFieldDeadline.getText());
    		
    		banco.adicionarTarefa(tarefa);
    		
    		initialize();
    		
    		textAreaAvisos.setText("Tarefa cadastrada com sucesso");
    	} else {
    		textAreaAvisos.setText("Dados de entrada inválidos");
    	}
    }
    
    @FXML
    void abrirTelaListagem(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(TelaListagemController.class.getResource("/gestao/visao/TelaListagem.fxml"));
    	AnchorPane page = (AnchorPane) loader.load();
    	
    	Stage listagemStage = new Stage();
    	listagemStage.setTitle("Listagem de Tarefas");
    	listagemStage.setResizable(false);
    	Scene scene = new Scene(page);
    	listagemStage.setScene(scene);
    	
    	TelaListagemController controller = loader.getController();
    	controller.setListagemStage(listagemStage);
    	listagemStage.showAndWait();
    }
}
