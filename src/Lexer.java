import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
  public static enum TokenType {
    // Token types cannot have underscores
    NUMBER("-?[0-9]+"), WHITESPACE("[ \t\f\r\n]+"), ATTRIBUTENAME("[a-z][0-9a-z]*\\.[a-z][0-9a-z]*"), NAME("[a-z][0-9a-z]*"),RESERVED("[A-Z]+"), BINARYOP("[*|/|+|-|,|.|;|(|)|[|]|<|>|=|\"]");

    public final String pattern;

    private TokenType(String pattern) {
      this.pattern = pattern;
    }
  }

  public static class Token {
    public TokenType type;
    public String data;

    public Token(TokenType type, String data) {
      this.type = type;
      this.data = data;
    }

    @Override
    public String toString() {
      return String.format("(%s %s)", type.name(), data);
    }
  }

  public static ArrayList<Token> lex(String input) {
    // The tokens to return
    ArrayList<Token> tokens = new ArrayList<Token>();

    // Lexer logic begins here
    StringBuffer tokenPatternsBuffer = new StringBuffer();
    for (TokenType tokenType : TokenType.values())
      tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

    // Begin matching tokens
    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find()) {
      if (matcher.group(TokenType.NUMBER.name()) != null) {
        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
        continue;
      } 
      else if (matcher.group(TokenType.ATTRIBUTENAME.name()) != null) {
          tokens.add(new Token(TokenType.ATTRIBUTENAME, matcher.group(TokenType.ATTRIBUTENAME.name())));
          continue;
      }
      else if (matcher.group(TokenType.NAME.name()) != null) {
        tokens.add(new Token(TokenType.NAME, matcher.group(TokenType.NAME.name())));
        continue;
      }
      else if (matcher.group(TokenType.RESERVED.name()) != null) {
          tokens.add(new Token(TokenType.RESERVED, matcher.group(TokenType.RESERVED.name())));
          continue;
        } 
      	else if (matcher.group(TokenType.BINARYOP.name()) != null) {
        tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
        continue;
      }      
      	else if (matcher.group(TokenType.WHITESPACE.name()) != null)
        continue;
      }

    return tokens;
  }

  //public static void main(String[] args) {
   // String input = "SELECT * FROM course WHERE grade = \"C\" AND [ exam > 70 OR project > 70 ] AND NOT ( exam * 30 + homework * 20 + project * 50 ) / 100 < 60";

    // Create tokens and print them
   // ArrayList<Token> tokens = lex(input);
   // for (Token token : tokens){
   //   System.out.println(token);
   //   System.out.println(token.data);
   // }
  //}
  

		public static void main(String[] args) {
		    //String input = "SELECT * FROM course WHERE grade = \"C\" AND [ exam > 70 OR project > 70 ] AND NOT ( exam * 30 + homework * 20 + project * 50 ) / 100 < 60";
			String select_statement = "SELECT (zy, ww) FROM course, course2 WHERE course.sid = course2.sid AND course.exam > course2.exam;";
		    // Create tokens and print them
		    //ArrayList<Token> tokens = lex(input);
			ArrayList<Token> tokens = lex(select_statement);
		    ArrayList<String> mylist = new ArrayList<String>();
		    for (Token token : tokens){
		    	mylist.add(token.data);
		    	System.out.println(token.data);
		    }
		    PTConstruct con = new PTConstruct(mylist);
		    ParseTree t;
		    t = con.construct();

		    
		  }
}