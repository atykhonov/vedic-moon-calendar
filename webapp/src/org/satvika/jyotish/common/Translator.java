package org.satvika.jyotish.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class Translator {

	private Properties properties;

	public Translator() {
		init();
	}

	private void init() {
		properties = new Properties();
		BufferedReader reader;
		try {

			ClassPathResource classPathResource = new ClassPathResource(
					"ru.properties");

			reader = new BufferedReader(new FileReader(
					classPathResource.getFile()));
			
			properties.load(reader);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String translate(String str) {
		return properties.getProperty(str);
	}
}