package pl.maciejnierzwicki.mcshop.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringUtils {
	
	/**
	* Converts given string to the list of strings.
	* The end of string is determined by occurrence of new line character (which is checked by comparing ASCII identifier).
	* If the original string has no new line characters the resulting list will contain one element with this string.
	* <p>
	*
	* @param  textarea a string
	* @return the resulting list of strings
	*/
	public static List<String> toList(String textarea) {
		List<String> result = new ArrayList<>();
		char[] chars = textarea.toCharArray();
		StringBuilder line = new StringBuilder();
		for(int i = 0; i < chars.length; i++) {
			char currentChar = chars[i];
			if(i + 1 < chars.length) {
				if(((int) currentChar == 13) && ((int) chars[i+1]) == 10) {
					result.add(line.toString());
					line = new StringBuilder();
					i++;
					continue;
				}
			}
			line.append(currentChar);
			if(i + 1 == chars.length) {
				result.add(line.toString());
			}
		}
		return result;
	}
	
	/**
	* Converts given List to String, adding new line character (ASCII code) after each element in list (excluding last element).
	* <p>
	*
	* @param  list a list
	* @return the resulting string
	*/
	public static String toString(List<String> list) {
		StringBuilder text = new StringBuilder();
		Iterator<String> it = list.iterator();
		while(it.hasNext()) {
			String line = it.next();
			text.append(line);
			if(it.hasNext()) {
				text.append((char) 13);
				text.append((char) 10);
			}
		}
		return text.toString();
	}

}
