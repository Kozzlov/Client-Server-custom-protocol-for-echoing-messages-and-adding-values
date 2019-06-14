import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Protocols {
	static public String get_response(String request) {
		if(!request.isEmpty()) {
			String[] parts = request.split(";");
			if(parts.length == 2) {
				//delete 0x00
				parts[1] = parts[1].replaceAll(new String(new byte[1]), "");
				// considering the numeric type of request 
				Pattern pattern = Pattern.compile("\\s*(\\d+)\\s*\\+\\s*(\\d+)");
				Matcher matcher = pattern.matcher(parts[1]);
				if (matcher.matches()) {
					int sum = Integer.valueOf(matcher.group(1)) + Integer.valueOf(matcher.group(2));
					return new String(" responce sum "+Integer.toString(sum));
				}
				// in case of string just returning the string 
				else return new String(" responce text "+parts[1]);
			}
		}
		return null;
	}   
}
