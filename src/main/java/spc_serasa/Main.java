package spc_serasa;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	public static final ConnectionFactory connectionFactory = new ConnectionFactory();

	public static BorderPane borderPane = new BorderPane();
	public static Scene scene = new Scene(borderPane, 690, 515);
	public static ComboBox<String> comboBox = new ComboBox<>();
	public static ComboBox<String> comboBoxSinc = new ComboBox<>();
	// TableView tipada com a superclasse Cliente
	public static TableView<Cliente> tableView = new TableView<>();
	public static ProgressIndicator progressIndicator = new ProgressIndicator();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Shopping Real SPC, Serasa e Protesto");

		Button listButtonSpc = new Button("Clientes SPC");
		Button listButtonSerasa = new Button("Clientes Serasa");
		Button listButtonProtesto = new Button("Clientes Protesto");
		Button buttonUpload = new Button("Upload");
		Button sincronizar = new Button("Sincronizar");

		comboBox.getItems().addAll("Spc", "Serasa", "Protesto");
		comboBox.setValue("Spc");
		comboBoxSinc.getItems().addAll("Spc", "Serasa", "Protesto");
		comboBoxSinc.setValue("Spc");

		progressIndicator.setVisible(false);
		progressIndicator.setPrefSize(24, 24);

		HBox hboxOpcao = new HBox(10);
		hboxOpcao.getChildren().addAll(listButtonSpc, listButtonSerasa, listButtonProtesto, comboBox, buttonUpload,
				comboBoxSinc, sincronizar, progressIndicator);

		listButtonSpc.setOnAction(e -> tabelaSpc());
		listButtonSerasa.setOnAction(e -> tabelaSerasa());
		listButtonProtesto.setOnAction(e -> tabelaProtesto());

		buttonUpload.setOnAction(e -> onUpload(primaryStage));
		
		sincronizar.setOnAction(event -> {
			String opcao = comboBoxSinc.getValue();
			
			if (opcao == null) {
				System.out.println("Selecione um opção antes de executar.");
				showError("Erro", "Selection um opção válida antes de sincronizar.");
				return;
			}
						
			Task<Void> task = new Task<Void>() {
				
				@Override
				protected Void call() throws Exception {
				
						switch (opcao) {
						case "Spc":
							atualizarSpc();
							break;
						case "Serasa":
							atualizarSerasa();
							break;
						
						case "Protesto":
							throw new UnsupportedOperationException("Sincronização Protesto não implementada");
							
							
						default: 
							throw new IllegalArgumentException("Opção inválida: " + opcao);	
					}				
					
					return null;
					
				}
				
				
			};
			
			progressIndicator.visibleProperty().bind(task.runningProperty());
			
			task.setOnSucceeded(evt -> {
				progressIndicator.visibleProperty().unbind();
				progressIndicator.setVisible(false);
				showInfo("Sucesso", "A sincronização foi concluída com sucesso!");
			});
			
			task.setOnFailed(evt -> {
				showError("Erro na Sincronização", "Houve um erro na sincronização.");
				progressIndicator.visibleProperty().unbind();
				progressIndicator.setVisible(false);
				Throwable erro = task.getException();
				showError("Erro na sincronização", erro == null ? "Erro desconhecido" : erro.getMessage());
				
				erro.printStackTrace();
			});
			
			
			Thread thread = new Thread(task, "sync-task");
			thread.setDaemon(true);
			thread.start();
			
		});
		
		

		borderPane.setTop(hboxOpcao);
		borderPane.setCenter(tableView);
		BorderPane.setMargin(tableView, new Insets(15, 0, 0, 0));

		primaryStage.setScene(scene);
		primaryStage.show();

		// carrega ao iniciar
		tabelaSpc();
	}

	private void onUpload(Stage owner) {
		String escolha = comboBox.getValue();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecione o arquivo para " + escolha);
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv"));
		File arquivo = fileChooser.showOpenDialog(owner);

		if (arquivo == null)
			return;

		Task<Void> task = new Task() {
			@Override
			protected Void call() throws Exception {
				List<String[]> linhas;
				try {
					linhas = Arquivo.ler(arquivo.getAbsolutePath());
				} catch (Exception ex) {
					throw new RuntimeException("Erro ao ler o arquivo CSV: " + ex.getMessage(), ex);
				}

				switch (escolha) {
				case "Spc":
					importSpc(linhas);
					break;
				case "Serasa":
					importSerasa(linhas);
					break;
				case "Protesto":
					importProtesto(linhas);
					break;
				default:
					throw new IllegalArgumentException("Tipo invalido: " + escolha);
				}
				return null;
			}
		};

		progressIndicator.visibleProperty().bind(task.runningProperty());

		task.setOnSucceeded(evt -> {
			progressIndicator.visibleProperty().unbind();
			progressIndicator.setVisible(false);
			showInfo("Importação", "Importação concluida com sucesso!");

			switch (escolha) {
			case "Spc":
				tabelaSpc();
				break;
			case "Serasa":
				tabelaSerasa();
				break;
			case "Protesto":
				tabelaProtesto();
				break;
			}
		});

		task.setOnFailed(evt -> {
			progressIndicator.visibleProperty().unbind();
			progressIndicator.setVisible(false);
			Throwable ex = task.getException();
			showError("Erro na importação", ex == null ? "Erro desconhecido" : ex.getMessage());
			if (ex != null)
				ex.printStackTrace();
		});

		Thread thread = new Thread(task, "import-task");
		thread.setDaemon(true);
		thread.start();
	}
	private void atualizarSerasa() throws SQLException {
		try(Connection conn1 = connectionFactory.getConnection();
				Connection conn2 = connectionFactory.getConnection("db2")) {
			SerasaDao serasaDaoOrigem = new SerasaDao(conn1);
			SerasaDao serasaDaoDestino = new SerasaDao(conn2);
			
			List<ClienteSerasa> clientes = serasaDaoOrigem.pegarNegativado();
			serasaDaoDestino.atualizaNegativado(clientes);
		}
	}

	private void atualizarSpc() throws SQLException {
		try (Connection conn1 = connectionFactory.getConnection();
				Connection conn2 = connectionFactory.getConnection("db2")) {
			SpcDao spcDaoOrigem = new SpcDao(conn1);
			SpcDao spcDaoDestino = new SpcDao(conn2);

			List<ClienteSpc> clientes = spcDaoOrigem.pegarNegativado();
			spcDaoDestino.atualizaNegativado(clientes);

		}
	}

	private void importSpc(List<String[]> linhas) {
		try (Connection conn = connectionFactory.getConnection()) {

			NegativadoSpc parser = new NegativadoSpc();
			List<ClienteSpc> clientes = parser.adiciona(linhas);
			SpcDao dao = new SpcDao(conn);
			dao.deletarTodos();
			dao.inserir(clientes);

		} catch (ErroImportacaoException ex1) {
			throw ex1;
		} catch (SQLException ex2) {
			throw new RuntimeException("Erro ao acessar o banco de dados durante a importação do SPC. ", ex2);
		}
	}

	private void importSerasa(List<String[]> linhas) {
		try (Connection conn = connectionFactory.getConnection()) {
			NegativadoSerasa parser = new NegativadoSerasa();
			List<ClienteSerasa> clientes = parser.adiciona(linhas);
			SerasaDao dao = new SerasaDao(conn);
			dao.deletarTodos();
			dao.inserir(clientes);

		} catch (Exception e) {
			throw new RuntimeException("Erro ao importar Serasa: " + e.getMessage(), e);
		}
	}

	private void importProtesto(List<String[]> linhas) {
		try (Connection conn = connectionFactory.getConnection()) {
			NegativadoProtesto parser = new NegativadoProtesto();
			List<ClienteProtesto> clientes = parser.adiciona(linhas);
			ProtestoDao dao = new ProtestoDao(conn);
			dao.deletarTodos();
			dao.inserir(clientes);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao importar Protesto: " + e.getMessage(), e);
		}
	}

	// ========== TABELA SPC ==========
	public static void tabelaSpc() {
		List<ClienteSpc> rows;
		try (Connection conn = connectionFactory.getConnection()) {
			SpcDao spcDao = new SpcDao(conn);
			rows = spcDao.listar();
		} catch (Exception e) {
			showError("Erro", "Falha ao carregar dados spc: " + e.getMessage());
			return;
		}

		// NOTE: declarar ObservableList<Cliente> permite setar no tableView<Cliente>
		ObservableList<Cliente> obsLista = FXCollections.observableArrayList(rows);
		tableView.getColumns().clear();
		tableView.setItems(obsLista);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

		TableColumn<Cliente, Long> colunaCodigoSpc = new TableColumn<>("Codigo SPC");
		colunaCodigoSpc.setCellValueFactory(new PropertyValueFactory<>("codigoSpc"));

		TableColumn<Cliente, String> colunaCpf = new TableColumn<>("Cpf/Cnpj");
		colunaCpf.setCellValueFactory(new PropertyValueFactory<>("cpfCnpj"));

		TableColumn<Cliente, String> colunaConsumidor = new TableColumn<>("Consumidor");
		colunaConsumidor.setCellValueFactory(new PropertyValueFactory<>("consumidor"));

		TableColumn<Cliente, String> colunaContrato = new TableColumn<>("Contrato");
		colunaContrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));

		TableColumn<Cliente, LocalDate> colunaDataVencimento = new TableColumn<>("Data Vencimento");
		colunaDataVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));

		TableColumn<Cliente, Double> colunaValorDebito = new TableColumn<>("Valor Debito");
		colunaValorDebito.setCellValueFactory(new PropertyValueFactory<>("valorDebito"));

		TableColumn<Cliente, LocalDate> colunaDataInclusao = new TableColumn<>("Data Inclusao");
		colunaDataInclusao.setCellValueFactory(new PropertyValueFactory<>("dataInclusao"));

		TableColumn<Cliente, LocalTime> horaInclusao = new TableColumn<>("Hora Inclusao");
		horaInclusao.setCellValueFactory(new PropertyValueFactory<>("horaInclusao"));

		TableColumn<Cliente, LocalDate> dataExclusao = new TableColumn<>("Data Exclusao");
		dataExclusao.setCellValueFactory(new PropertyValueFactory<>("dataExclusao"));

		TableColumn<Cliente, String> tipoNotificacao = new TableColumn<>("Tipo Notificacao");
		tipoNotificacao.setCellValueFactory(new PropertyValueFactory<>("tipoNotificacao"));

		TableColumn<Cliente, Long> codigoNotificacao = new TableColumn<>("Codigo Notificacao");
		codigoNotificacao.setCellValueFactory(new PropertyValueFactory<>("codigoNotificacao"));

		TableColumn<Cliente, Integer> codigoAssociado = new TableColumn<>("Codigo Associado");
		codigoAssociado.setCellValueFactory(new PropertyValueFactory<>("codigoAssociado"));

		tableView.getColumns().addAll(colunaCodigoSpc, colunaCpf, colunaConsumidor, colunaContrato,
				colunaDataVencimento, colunaValorDebito, colunaDataInclusao, horaInclusao, dataExclusao,
				tipoNotificacao, codigoNotificacao, codigoAssociado);
	}

	// ========== TABELA SERASA ==========
	public static void tabelaSerasa() {
		List<ClienteSerasa> rows;
		try (Connection conn = connectionFactory.getConnection()) {
			SerasaDao serasaDao = new SerasaDao(conn);
			rows = serasaDao.listar();
		} catch (Exception e) {
			showError("Error", "Falha ao carregar dados Serasa: " + e.getMessage());
			return;
		}

		ObservableList<Cliente> obsLista = FXCollections.observableArrayList(rows);
		tableView.getColumns().clear();
		tableView.setItems(obsLista);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

		TableColumn<Cliente, String> colunaStatus = new TableColumn<>("Status");
		colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

		TableColumn<Cliente, Long> colunaCodigo = new TableColumn<>("Id");
		colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Cliente, String> colunaNome = new TableColumn<>("Nome do Devedor");
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeDevedor"));

		TableColumn<Cliente, String> colunaCpf = new TableColumn<>("Documento");
		colunaCpf.setCellValueFactory(new PropertyValueFactory<>("documento"));

		TableColumn<Cliente, String> colunaTipo = new TableColumn<>("Tipo Pessoa");
		colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoPessoa"));

		TableColumn<Cliente, String> colunaNatureza = new TableColumn<>("Natureza");
		colunaNatureza.setCellValueFactory(new PropertyValueFactory<>("natureza"));

		TableColumn<Cliente, Double> colunaValor = new TableColumn<>("Valor");
		colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

		TableColumn<Cliente, LocalDate> colunaDataInclusao = new TableColumn<>("Data de Cadastro");
		colunaDataInclusao.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));

		TableColumn<Cliente, LocalTime> colunaHora = new TableColumn<>("Hora");
		colunaHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

		TableColumn<Cliente, LocalDate> colunaDataVenc = new TableColumn<>("Data Vencimento");
		colunaDataVenc.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));

		TableColumn<Cliente, String> colunaOperacao = new TableColumn<>("Operação");
		colunaOperacao.setCellValueFactory(new PropertyValueFactory<>("operacao"));

		tableView.getColumns().addAll(colunaStatus, colunaCodigo, colunaNome, colunaTipo, colunaCpf, colunaNatureza,
				colunaValor, colunaDataInclusao, colunaHora, colunaDataVenc, colunaOperacao);
	}

	// ========== TABELA PROTESTO ==========
	public static void tabelaProtesto() {
		List<ClienteProtesto> rows;
		try (Connection conn = connectionFactory.getConnection()) {
			ProtestoDao protestoDao = new ProtestoDao(conn);
			rows = protestoDao.listar();
		} catch (Exception e) {
			showError("Erro", "Falha ao carregar dados Protesto: " + e.getMessage());
			return;
		}

		ObservableList<Cliente> obsLista = FXCollections.observableArrayList(rows);
		tableView.getColumns().clear();
		tableView.setItems(obsLista);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

		TableColumn<Cliente, Long> colunaPedido = new TableColumn<>("Pedido");
		colunaPedido.setCellValueFactory(new PropertyValueFactory<>("pedido"));

		TableColumn<Cliente, String> colunaComarcaCartorio = new TableColumn<>("Comarca Cartorio");
		colunaComarcaCartorio.setCellValueFactory(new PropertyValueFactory<>("comarcaCartorio"));

		TableColumn<Cliente, LocalDate> colunaDataSolicitacao = new TableColumn<>("Data da Solicitação");
		colunaDataSolicitacao.setCellValueFactory(new PropertyValueFactory<>("dataSolicitacao"));

		TableColumn<Cliente, String> colunaComarcaDevedor = new TableColumn<>("Comarca Devedor");
		colunaComarcaDevedor.setCellValueFactory(new PropertyValueFactory<>("comarcaDevedor"));

		TableColumn<Cliente, String> colunaDevedor = new TableColumn<>("Devedor");
		colunaDevedor.setCellValueFactory(new PropertyValueFactory<>("devedor"));

		TableColumn<Cliente, String> colunaDocDevedor = new TableColumn<>("Doc Devedor");
		colunaDocDevedor.setCellValueFactory(new PropertyValueFactory<>("docDevedor"));

		TableColumn<Cliente, String> colunaNumeroTitulo = new TableColumn<>("Nº Título");
		colunaNumeroTitulo.setCellValueFactory(new PropertyValueFactory<>("numeroTitulo"));

		TableColumn<Cliente, Double> colunaValorTitulo = new TableColumn<>("Valor Título");
		colunaValorTitulo.setCellValueFactory(new PropertyValueFactory<>("valorTitulo"));

		TableColumn<Cliente, Double> colunaValorProtesto = new TableColumn<>("Valor Protestado");
		colunaValorProtesto.setCellValueFactory(new PropertyValueFactory<>("valorProtesto"));

		TableColumn<Cliente, Long> colunaProtocolo = new TableColumn<>("Protocolo");
		colunaProtocolo.setCellValueFactory(new PropertyValueFactory<>("protocolo"));

		TableColumn<Cliente, LocalDate> colunaDataProtocolo = new TableColumn<>("Data Protocolo");
		colunaDataProtocolo.setCellValueFactory(new PropertyValueFactory<>("dataProtocolo"));

		TableColumn<Cliente, String> colunaEspecie = new TableColumn<>("Espécie");
		colunaEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));

		TableColumn<Cliente, String> colunaStatusPedido = new TableColumn<>("Status Pedido");
		colunaStatusPedido.setCellValueFactory(new PropertyValueFactory<>("statusPedido"));

		TableColumn<Cliente, String> colunaIrregularidade = new TableColumn<>("Irregularidade");
		colunaIrregularidade.setCellValueFactory(new PropertyValueFactory<>("irregularidade"));

		TableColumn<Cliente, String> colunaOcorrenciaTitulo = new TableColumn<>("Ocorrência Título");
		colunaOcorrenciaTitulo.setCellValueFactory(new PropertyValueFactory<>("ocorrenciaTitulo"));

		TableColumn<Cliente, LocalDate> colunaDataOcorrencia = new TableColumn<>("Data Ocorrência");
		colunaDataOcorrencia.setCellValueFactory(new PropertyValueFactory<>("dataOcorrencia"));

		tableView.getColumns().addAll(colunaPedido, colunaComarcaCartorio, colunaDataSolicitacao, colunaComarcaDevedor,
				colunaDevedor, colunaDocDevedor, colunaNumeroTitulo, colunaValorTitulo, colunaValorProtesto,
				colunaProtocolo, colunaDataProtocolo, colunaEspecie, colunaStatusPedido, colunaIrregularidade,
				colunaOcorrenciaTitulo, colunaDataOcorrencia);
	}

	public static void showError(String title, String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		});
	}

	public static void showInfo(String title, String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		});
	}
}
