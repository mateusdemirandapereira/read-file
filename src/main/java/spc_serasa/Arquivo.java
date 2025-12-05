package spc_serasa;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;



public class Arquivo {
		

	public static List<String[]> ler(String arquivo) throws IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(arquivo))) {
			return  reader.readAll();
			
		} 

	}

}
