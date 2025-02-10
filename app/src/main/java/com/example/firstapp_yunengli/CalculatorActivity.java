package com.example.firstapp_yunengli;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class CalculatorActivity extends AppCompatActivity {

    private EditText calcDisplay;
    private String currentInput = "CALC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity);

        calcDisplay = findViewById(R.id.calcDisplay);
        calcDisplay.setText(currentInput);

        int[] buttonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonPlus, R.id.buttonMinus
        };

        String[] buttonValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-"};

        for (int i = 0; i < buttonIds.length; i++) {
            int id = buttonIds[i];
            String value = buttonValues[i];
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                if (currentInput.equals("CALC")) {
                    currentInput = value;
                } else {
                    currentInput += value;
                }
                calcDisplay.setText(currentInput);
            });
        }

        Button buttonX = findViewById(R.id.buttonX);
        buttonX.setOnClickListener(v -> {
            if (!currentInput.equals("CALC") && !currentInput.isEmpty()) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                if (currentInput.isEmpty()) {
                    currentInput = "CALC";
                }
                calcDisplay.setText(currentInput);
            }
        });

        Button buttonEquals = findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(v -> {
            try {
                if (!currentInput.equals("CALC")) {
                    double result = evaluateExpression(currentInput);
                    if (result == (long) result) {
                        currentInput = String.valueOf((long) result);
                    } else {
                        currentInput = String.valueOf(result);
                    }
                }
            } catch (Exception e) {
                currentInput = "Error"; // Show error if the expression is invalid
            }
            calcDisplay.setText(currentInput); // Update the display
        });
    }

    private double evaluateExpression(String expression) {
        return evaluatePostfix(infixToPostfix(expression.replace("x", "*")));
    }

    private String infixToPostfix(String exp) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                result.append(c);
            } else {
                result.append(" ");
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            result.append(" ").append(stack.pop());
        }

        return result.toString();
    }

    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        stack.push(a / b);
                        break;
                }
            }
        }

        return stack.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return -1;
    }
}