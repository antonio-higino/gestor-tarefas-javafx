package gestao.controle;

import java.io.IOException;

import gestao.dao.TarefaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TelaEdicaoController {
	
	ObservableList<String> responsavelList = FXCollections.observableArrayList("Select","Ana","Antonio","Carlos","João");
	ObservableList<String> prioridadeList = FXCollections.observableArrayList("Select","Alta","Média","Baixa");
	
	private Stage edicaoStage;
	
	@FXML
    private TextField textFieldTitulo;

    @FXML
    private ChoiceBox<String> choiceBoxPrioridade;

    @FXML
    private ChoiceBox<String> choiceBoxResponsavel;

    @FXML
    private TextField textFieldDeadline;

    @FXML
    private TextArea textAreaAvisos;

    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private Button botaoEditar;

    @FXML
    private TextField textFieldNumero;
    
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

    @FXML
    void editarTarefa(ActionEvent event) throws IOException {
    	try {
    		TarefaDAO banco = TarefaDAO.getInstance();
    		
    		int id = Integer.parseInt(textFieldNumero.getText());
    		
    		if(banco.tarefaExiste(id)) {
    			
    			String titulo = textFieldTitulo.getText();
    			String descricao = textAreaDescricao.getText();
    			String deadline = textFieldDeadline.getText();
    			
    			boolean editou = false;
    			
    			if(titulo != null) {
    				banco.getTarefaEspecifica(id).setTitulo(titulo);
    				editou = true;
    			}
    			
    			if(descricao != null) {
    				banco.getTarefaEspecifica(id).setDescricao(descricao);
    				editou = true;
    			}
    			
    			if(deadline != null){
    				banco.getTarefaEspecifica(id).setDeadline(deadline);
    				editou = true;
    			}
    			
    			if(choiceBoxResponsavel.getValue() != "Select") {
    				banco.getTarefaEspecifica(id).setResponsavel(choiceBoxResponsavel.getValue());
    				editou = true;
    			}
    			if(choiceBoxPrioridade.getValue() != "Select") {
    				banco.getTarefaEspecifica(id).setPrioridade(choiceBoxPrioridade.getValue());
    				editou = true;
    			}
    			
    			if(editou) {
    				textAreaAvisos.setText("\nTarefa #" + id + " foi editada\n");
    			} else {
    				textAreaAvisos.setText("\nTarefa #" + id + " não foi editada\n");
    			}	
    		} else {
    			textAreaAvisos.setText("\nTarefa #" + id + " não existe\n");
    		}
    		
    		initialize();
    		
    	} catch(NumberFormatException ex) {
    		textAreaAvisos.setText("Número inválido");
    		initialize();
    	}
    }

	public void setEdicaoStage(Stage edicaoStage) {
		this.edicaoStage = edicaoStage;
	}

}
