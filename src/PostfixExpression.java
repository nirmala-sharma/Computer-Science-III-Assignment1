import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class PostfixExpression {

    public static void main(String[] args) {

        System.out.println("Hello! This is a postfix expression calculator.");

        try (BufferedReader br = new BufferedReader(new FileReader("in.dat"))) {
            String expression;

            while ((expression = br.readLine()) != null) {
                if (expression.trim().isEmpty()) continue;

                double result = evaluatePostfix(expression);
                System.out.println("The value of \"" + expression + "\" is " + result);
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file in.dat: " + e.getMessage());
        }
        catch (RuntimeException e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
        }

        System.out.println("Bye-bye!");
    }

    public static double evaluatePostfix(String expression) {
        String[] tokens = expression.split("\\s+");
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("_")) {
                double a = stack.pop();
                stack.push(-a);
            } else if (token.equals("#")) {
                double a = stack.pop();
                stack.push(Math.sqrt(a));
            } else {                                  // binary operators
                double b = stack.pop();
                double a = stack.pop();
                stack.push(calculation(a, b, token));
            }
        }

        if (stack.size() != 1) {
            throw new RuntimeException("Invalid postfix expression: " + expression);
        }

        return stack.pop();
    }

    static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static double calculation(double a, double b, String token) {
        return switch (token) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "^" -> Math.pow(a, b);
            default -> throw new RuntimeException("Unknown operator: " + token);
        };
    }
}
