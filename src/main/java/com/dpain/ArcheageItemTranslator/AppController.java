package com.dpain.ArcheageItemTranslator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AppController implements Initializable {

	@FXML
	private Button btnTranslate;
	@FXML
	private TextField txfInput;
	@FXML
	private TextField txfOutput;
	@FXML
	private Text lblProgress;
	@FXML
	private CheckBox ckbChrome;

	private Translator translator;

	public AppController(Translator translator) {
		this.translator = translator;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnTranslate.setOnAction(this::handleTranslate);
	}

	private void handleTranslate(ActionEvent event) {
		lblProgress.setText("서버 연결중..");

		Thread translateThread = new Thread() {
			public void run() {
				String result;
				try {
					result = translator.getTranslation(txfInput.getText(), ckbChrome.isSelected());
					txfOutput.setText(result);
					lblProgress.setText("번역 완료!");
				} catch (Exception e) {
					lblProgress.setText(e.getMessage());
				}
				
			}
		};

		translateThread.start();
	}
}
